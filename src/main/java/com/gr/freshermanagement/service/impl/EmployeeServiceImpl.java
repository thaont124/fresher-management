package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.exception.department.DepartmentNotFoundException;
import com.gr.freshermanagement.repository.*;
import com.gr.freshermanagement.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public NewFresherResponse createNewEmployee(NewEmployeeRequest request, Long centerId) {
        if ("fresher".equalsIgnoreCase(request.getPosition())) {
            return createNewFresher(request, centerId);
        } else {
            throw new IllegalArgumentException("Unsupported position: " + request.getPosition());
        }
    }


    private String generateEmployeeCode(String position, Long id) {
        if(position.equalsIgnoreCase("FRESHER")){
            return "FRS" + id;
        }

        return "EMP" + id;
    }

    private String createUsername(String name, LocalDate dob, Long id) {
        // Convert name to ASCII (remove diacritics)
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String nameWithoutDiacritics = pattern.matcher(normalized).replaceAll("");

        // Split name and get the last part
        String[] nameParts = nameWithoutDiacritics.split(" ");
        String lastName = nameParts[nameParts.length - 1].toLowerCase();

        // Get year from dob
        int year = dob.getYear();

        // Combine to form username
        return lastName + year + id;
    }

    @Transactional
    public NewFresherResponse createNewFresher(NewEmployeeRequest request, Long centerId) {
        // Find department
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        Fresher fresher = new Fresher();
        fresher.setName(request.getName());
        fresher.setDob(request.getDob());
        fresher.setAddress(request.getAddress());
        fresher.setPhone(request.getPhone());
        fresher.setGender(Gender.valueOf(request.getGender()));
        fresher.setEmail(request.getEmail());
        fresher.setDepartment(department);
        fresher.setStatus(EmployeeStatus.ACTIVE);
        fresher.setFresherStatus(FresherStatus.WAITING);

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
        fresher.setAccount(account);
        return new NewFresherResponse(fresher, username, username); // Assuming NewFresherResponse is defined appropriately
    }




}
