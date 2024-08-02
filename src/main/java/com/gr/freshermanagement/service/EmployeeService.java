package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.employee.EmployeeResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface EmployeeService {
    @Transactional
    void deactivateStatus(Long fresherId);

    @Transactional
    EmployeeResponse updateEmployeeAsAdmin(Account account, UpdateEmployeeRequest adminUpdateEmployeeRequest);


    @Transactional
    EmployeeResponse updateEmployee(String username, UpdateEmployeeRequest employeeDetails);

    @Transactional
    EmployeeResponse updateEmployeeAsEmployee(Account account, UpdateEmployeeRequest employeeUpdateEmployeeRequest);
    Optional<Employee> findFresherByAccountUsername(String username);
    Employee findEmployeeByUsername(String username);

    boolean checkEmailWithUsername(String username, String email);

    void changeAvatar(String username, MultipartFile avatar);

    EmployeeResponse getEmployee(String username);
}
