package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.repository.DepartmentRepository;
import com.gr.freshermanagement.repository.EmployeeLanguageRepository;
import com.gr.freshermanagement.repository.FresherRepository;
import com.gr.freshermanagement.repository.LanguageRepository;
import com.gr.freshermanagement.service.ExcelService;
import com.gr.freshermanagement.service.FresherService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FresherServiceImpl extends ExcelService<Fresher> implements FresherService {
    private final FresherRepository fresherRepository;
    private final DepartmentRepository departmentRepository;
    private final LanguageRepository languageRepository;
    private final EmployeeLanguageRepository employeeLanguageRepository;

    @Override
    public List<EmployeeResponse> addListFresher(MultipartFile file) {
        //get requests from excel
        return null;

    }

    @Override
    public Page<Fresher> getFreshers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fresherRepository.findAll(pageable);
    }

    @Override
    protected Fresher mapRowToEntity(Row row, String sheetName) {
       //get information
        String name = row.getCell(1).getStringCellValue();
        LocalDate dob = row.getCell(2).getLocalDateTimeCellValue().toLocalDate();
        String address = row.getCell(3).getStringCellValue();
        String phone = row.getCell(4).getStringCellValue();
        Gender gender = Gender.valueOf(row.getCell(5).getStringCellValue());
        String email = row.getCell(6).getStringCellValue();

        //check fresher exist
        Fresher fresher = fresherRepository.findByEmail(email);
        if (fresher == null){
            fresher = Fresher.builder()
                    .name(name)
                    .dob(dob)
                    .address(address)
                    .phone(phone)
                    .gender(gender)
                    .email(email)
                    .build();
            fresher = fresherRepository.save(fresher);
        }


        Language language = languageRepository.findByLanguageName(sheetName).orElseGet(() -> {
            Language newLanguage = Language.builder()
                    .languageName(sheetName)
                    .build();
            return languageRepository.save(newLanguage);
        });

        EmployeeLanguage employeeLanguage = EmployeeLanguage.builder()
                .language(language)
                .employee(fresher)
                .build();
        employeeLanguage = employeeLanguageRepository.save(employeeLanguage);

        return fresher;
    }

    @Override
    protected void processData(List<Fresher> data) {

    }
}
