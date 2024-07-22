package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Employee;
import jakarta.transaction.Transactional;

import java.util.Optional;

public interface EmployeeService {
    @Transactional
    void deactivateStatus(Long fresherId);

    @Transactional
    EmployeeResponse updateEmployeeAsAdmin(Account account, UpdateEmployeeRequest adminUpdateEmployeeRequest);


    @Transactional
    EmployeeResponse updateEmployee(String username, Employee employeeDetails);

    @Transactional
    EmployeeResponse updateEmployeeAsEmployee(Account account, UpdateEmployeeRequest employeeUpdateEmployeeRequest);
    Optional<Employee> findFresherByAccountUsername(String username);
    Employee findEmployeeByUsername(String username);

}
