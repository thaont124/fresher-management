package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Employee;
import jakarta.transaction.Transactional;

public interface EmployeeService {
    @Transactional
    void deActiveStatus(Long fresherId);

    @Transactional
    EmployeeResponse updateEmployeeAsAdmin(Account account, UpdateEmployeeRequest adminUpdateEmployeeRequest);


    @Transactional
    EmployeeResponse updateEmployeeAsEmployee(Account account, UpdateEmployeeRequest employeeUpdateEmployeeRequest);
}
