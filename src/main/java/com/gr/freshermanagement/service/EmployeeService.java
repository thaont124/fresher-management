package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Employee;
import jakarta.transaction.Transactional;

public interface EmployeeService {
    @Transactional
    EmployeeResponse updateEmployeeAsAdmin(Long employeeId, UpdateEmployeeRequest adminUpdateEmployeeRequest);

    @Transactional
    EmployeeResponse updateEmployeeAsEmployee(Long employeeId, UpdateEmployeeRequest employeeUpdateEmployeeRequest);
}
