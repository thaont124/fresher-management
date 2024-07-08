package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.employee.EmployeeUpdateRequest;
import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.exception.department.DepartmentNotFoundException;
import com.gr.freshermanagement.exception.role.RoleNotFoundException;
import com.gr.freshermanagement.repository.*;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.utils.ExcelUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public NewFresherResponse createNewEmployee(NewEmployeeRequest request) {
        if ("fresher".equalsIgnoreCase(request.getPosition())) {
            return createNewFresher(request);
        } else {
            throw new IllegalArgumentException("Unsupported position: " + request.getPosition());
        }
    }

    @Override
    public EmployeeResponse getInfo(Long accountId) {
        return null;
    }

    @Override
    @Transactional
    public EmployeeResponse updateInfo(EmployeeUpdateRequest request, Long accountId) {

        return new EmployeeResponse();
    }

    @Override
    @Transactional
    public NewFresherResponse createListEmployee(MultipartFile file) throws IOException {
        List<NewEmployeeRequest> listRequest = ExcelUtility.excelToFresherList(file.getInputStream());

        return null;
    }

    private String generateEmployeeCode(String position, Long id) {
        return position.equalsIgnoreCase("FRESHER") ? "FRS" + id : "EMP" + id;
    }

    private String createUsername(String name, LocalDate dob, Long id) {
        // Convert name to ASCII (remove diacritics)
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String nameWithoutDiacritics = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
                .matcher(normalized)
                .replaceAll("");

        // Split name and get the last part
        String[] nameParts = nameWithoutDiacritics.split(" ");
        String lastName = nameParts[nameParts.length - 1].toLowerCase();

        // Get year from dob
        int year = dob.getYear();

        // Combine to form username
        return lastName + year + id;
    }

    @Transactional
    public NewFresherResponse createNewFresher(NewEmployeeRequest request) {
        // Find department
        Department department = departmentRepository.findByCode(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        // Set info fresher
        Fresher fresher = new Fresher();
        setFresherInfo(fresher, request, department);

        // Save Fresher entity to generate ID
        fresher = employeeRepository.save(fresher);

        fresher.setEmployeeCode(generateEmployeeCode(request.getPosition(), fresher.getId()));

        // Generate username and password for the account
        String username = createUsername(fresher.getName(), fresher.getDob(), fresher.getId());
        String encodedPassword = passwordEncoder.encode(username); // Encode password

        // Create Account entity
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(encodedPassword);
        account.setEmployee(fresher);
        // Save Account entity
        accountRepository.save(account);

        // Set the account in fresher
        Role roleFresher = roleRepository.findByName("ROLE_FRESHER").orElseThrow(
                () -> new RoleNotFoundException("Not found role name ROLE_FRESHER"));
        fresher.setAccount(account);
        AccountRole accountRole = new AccountRole(account, roleFresher);
        accountRoleRepository.save(accountRole);

        return new NewFresherResponse(fresher, username, username);
    }

    private void setFresherInfo(Fresher fresher, NewEmployeeRequest request, Department department) {
        fresher.setName(request.getName());
        fresher.setDob(request.getDob());
        fresher.setAddress(request.getAddress());
        fresher.setPhone(request.getPhone());
        fresher.setGender(Gender.valueOf(request.getGender()));
        fresher.setEmail(request.getEmail());
        fresher.setDepartment(department);
        fresher.setStatus(EmployeeStatus.ACTIVE);
        fresher.setFresherStatus(FresherStatus.WAIT);
    }
}
