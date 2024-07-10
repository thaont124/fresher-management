package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.Gender;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.utils.MapperUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public EmployeeResponse updateEmployeeAsAdmin(Long employeeId, UpdateEmployeeRequest adminUpdateEmployeeRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        updateCommonFields(employee, adminUpdateEmployeeRequest);

        if (adminUpdateEmployeeRequest.getEmail() != null) {
            employee.setEmail(adminUpdateEmployeeRequest.getEmail());
        }

        return MapperUtils.toDTO(employee, EmployeeResponse.class);
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployeeAsEmployee(Long employeeId, UpdateEmployeeRequest employeeUpdateEmployeeRequest) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        updateCommonFields(employee, employeeUpdateEmployeeRequest);

        return MapperUtils.toDTO(employee, EmployeeResponse.class);

    }

    private void updateCommonFields(Employee employee, UpdateEmployeeRequest updateRequest) {
        if (updateRequest.getName() != null) {
            employee.setName(updateRequest.getName());
        }
        if (updateRequest.getDob() != null) {
            employee.setDob(updateRequest.getDob());
        }
        if (updateRequest.getAddress() != null) {
            employee.setAddress(updateRequest.getAddress());
        }
        if (updateRequest.getPhone() != null) {
            employee.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getGender() != null) {
            employee.setGender(Gender.valueOf(updateRequest.getGender()));
        }
    }
}
