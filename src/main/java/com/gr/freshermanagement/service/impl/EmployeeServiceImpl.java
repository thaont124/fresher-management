package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.repository.FresherRepository;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.utils.MapperUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final FresherRepository fresherRepository;

    @Transactional
    @Override
    public void deActiveStatus(Long fresherId) {
        Fresher fresher = fresherRepository.findById(fresherId)
                .orElseThrow(() -> new NotFoundException("Fresher not found with id: " + fresherId));
        fresher.setStatus(EmployeeStatus.TERMINATED);
    }
    @Transactional
    @Override
    public EmployeeResponse updateEmployeeAsAdmin(Account account, UpdateEmployeeRequest adminUpdateEmployeeRequest) {
        Employee employee = employeeRepository.findByAccount(account)
                .orElseGet(() -> createNewEmployee(adminUpdateEmployeeRequest));

        updateCommonFields(employee, adminUpdateEmployeeRequest);

        if (adminUpdateEmployeeRequest.getEmail() != null) {
            employee.setEmail(adminUpdateEmployeeRequest.getEmail());
        }

        return MapperUtils.toDTO(employee, EmployeeResponse.class);
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployeeAsEmployee(Account account, UpdateEmployeeRequest employeeUpdateEmployeeRequest) {
        Employee employee = employeeRepository.findByAccount(account)
                .orElseGet(() -> createNewEmployee(employeeUpdateEmployeeRequest));

        updateCommonFields(employee, employeeUpdateEmployeeRequest);
        return MapperUtils.toDTO(employee, EmployeeResponse.class);

    }
    private Employee createNewEmployee(UpdateEmployeeRequest request) {
        Employee newEmployee = Employee.builder()
                .name(request.getName())
                .dob(request.getDob())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .gender(Gender.valueOf(request.getGender()))
                .status(EmployeeStatus.WORKING)
                .build();
        return employeeRepository.save(newEmployee);
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
