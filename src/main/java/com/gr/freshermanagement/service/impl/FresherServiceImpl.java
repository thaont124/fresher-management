package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.repository.*;
import com.gr.freshermanagement.service.ExcelService;
import com.gr.freshermanagement.service.FresherService;
import com.gr.freshermanagement.utils.MapperUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FresherServiceImpl extends ExcelService<NewEmployeeRequest> implements FresherService {
    private static final Logger logger = LoggerFactory.getLogger(FresherServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final LanguageRepository languageRepository;
    private final EmployeeLanguageRepository employeeLanguageRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public List<EmployeeResponse> addListFresher(MultipartFile file) {
        //get requests from excel
        try {
            // Convert MultipartFile to File
            File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(file.getBytes());
            }

            // Import file and process data
            importFile(convFile);

            // Retrieve all freshers and convert to EmployeeResponse DTOs
            List<Employee> freshers = employeeRepository.findAll();
            return MapperUtils.toDTOs(freshers, EmployeeResponse.class);
        } catch (IOException e) {
            logger.error("Error processing the Excel file", e);
            return null;
        }
    }

    @Override
    public List<EmployeeResponse> getFreshers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> fresherPage = employeeRepository.findAll(pageable);
        return fresherPage.stream()
                .map(fresher -> MapperUtils.toDTO(fresher, EmployeeResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    protected NewEmployeeRequest mapRowToEntity(Row row) {
        //get information
        String name = row.getCell(1).getStringCellValue();
        LocalDate dob = row.getCell(2).getLocalDateTimeCellValue().toLocalDate();
        String address = row.getCell(3).getStringCellValue();
        String phone = row.getCell(4).getStringCellValue();
        String gender = row.getCell(5).getStringCellValue();
        String email = row.getCell(6).getStringCellValue();
        String languageName = row.getCell(7).getStringCellValue();

        return NewEmployeeRequest.builder()
                .name(name)
                .dob(dob)
                .address(address)
                .phone(phone)
                .gender(gender)
                .email(email)
                .languages(Collections.singletonList(languageName))
                .build();
    }

    @Override
    @Transactional
    protected void processData(List<NewEmployeeRequest> data) {
        for (NewEmployeeRequest fresherRequest : data) {
            Employee existingFresher = employeeRepository.findByEmail(fresherRequest.getEmail());
            if (existingFresher == null) {

                existingFresher = createNewFresher(fresherRequest);
                employeeRepository.save(existingFresher);

                Account newAccount = createNewAccountForFresher(existingFresher, fresherRequest);
                accountRepository.save(newAccount);

                // set role for account
                assignRoleToAccount(newAccount);

                // set account for employee
                existingFresher.setAccount(newAccount);
                employeeRepository.save(existingFresher);
            }

            saveLanguageWithEmployee(existingFresher, fresherRequest.getLanguages());
        }
    }

    private Employee createNewFresher(NewEmployeeRequest fresherRequest) {
        return Employee.builder()
                .name(fresherRequest.getName())
                .dob(fresherRequest.getDob())
                .address(fresherRequest.getAddress())
                .email(fresherRequest.getEmail())
                .phone(fresherRequest.getPhone())
                .gender(Gender.valueOf(fresherRequest.getGender()))
                .position("Fresher")
                .status(EmployeeStatus.EDUCATING)
                .build();
    }

    private Account createNewAccountForFresher(Employee fresher, NewEmployeeRequest fresherRequest) {
        return Account.builder()
                .username(fresherRequest.getEmail())
                .password(passwordEncoder.encode(generatePassword(fresherRequest.getName(), fresherRequest.getDob())))
                .employee(fresher)
                .status(Account.AccountStatus.NEW)
                .build();
    }

    private void assignRoleToAccount(Account account) {
        Role role = roleRepository.findByName("ROLE_FRESHER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_FRESHER").build()));
        AccountRole accountRole = AccountRole.builder()
                .account(account)
                .role(role)
                .build();
        accountRoleRepository.save(accountRole);
    }

    private void saveLanguageWithEmployee(Employee fresher, List<String> languages) {
        for (String languageRequest : languages) {
            Language language = languageRepository.findByLanguageName(languageRequest)
                    .orElseGet(() -> languageRepository.save(Language.builder().languageName(languageRequest).build()));

            EmployeeLanguage existingEmployeeLanguage = employeeLanguageRepository
                    .findByEmployeeAndLanguage(fresher, language);
            if (existingEmployeeLanguage == null) {
                EmployeeLanguage newEmployeeLanguage = EmployeeLanguage.builder()
                        .employee(fresher)
                        .language(language)
                        .build();
                employeeLanguageRepository.save(newEmployeeLanguage);
            }
        }
    }

    private String generatePassword(String name, LocalDate dob) {
        String[] nameParts = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .split(" ");
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < nameParts.length - 1; i++) {
            if (!nameParts[i].isEmpty()) {
                initials.append(nameParts[i].charAt(0));
            }
        }
        initials.append(nameParts[nameParts.length - 1]);
        String dobString = dob.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        return initials + dobString;
    }


}
