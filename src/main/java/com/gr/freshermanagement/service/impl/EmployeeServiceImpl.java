package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.repository.RoleRepository;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.utils.MapperUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public void deactivateStatus(Long accountId) {
//        deactivate account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));
        account.setStatus(Account.AccountStatus.INACTIVE);

//        deactivateStatus employee
        Employee employee = employeeRepository.findByAccount(account)
                .orElseThrow(() -> new NotFoundException("Employee not found with accountId: " + accountId));
        employee.setStatus(EmployeeStatus.TERMINATED);
    }
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
    public EmployeeResponse updateEmployee(String username, Employee employeeDetails) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Employee employee = account.getEmployee();
            if (employee == null) {
                employee = new Employee();
                employee.setAccount(account);
                account.setEmployee(employee);
            }
            employee.setName(employeeDetails.getName());
            employee.setDob(employeeDetails.getDob());
            employee.setAddress(employeeDetails.getAddress());
            employee.setPhone(employeeDetails.getPhone());
            employee.setGender(employeeDetails.getGender());
            employee.setEmail(employeeDetails.getEmail());
            employee.setPosition(employeeDetails.getPosition());
            employee.setModifiedTime(employeeDetails.getModifiedTime());
            employee.setStatus(employeeDetails.getStatus());

            Employee savedEmployee = employeeRepository.save(employee);
            accountRepository.save(account);
            return MapperUtils.toDTO(savedEmployee, EmployeeResponse.class);
        } else {
            throw new RuntimeException("Account not found for username: " + username);
        }
    }

    @Transactional
    @Override
    public EmployeeResponse updateEmployeeAsEmployee(Account account, UpdateEmployeeRequest employeeUpdateEmployeeRequest) {
        Employee employee = employeeRepository.findByAccount(account)
                .orElseGet(() -> createNewEmployee(employeeUpdateEmployeeRequest));

        updateCommonFields(employee, employeeUpdateEmployeeRequest);
        return MapperUtils.toDTO(employee, EmployeeResponse.class);

    }

    @Override
    public Optional<Employee> findFresherByAccountUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Account not found with username: " + username)
        );

        List<Role> roles = roleRepository.findRolesByAccountUsername(username);
        boolean isFresher = roles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_FRESHER"));
        if(!isFresher){
            throw new AccessDeniedException("The employee is not a Fresher");
        }
        return employeeRepository.findByAccount(account);
    }

    @Override
    public Employee findEmployeeByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("Account not found with username: " + username)
        );
        return employeeRepository.findByAccount(account).orElseThrow(
                () -> new NotFoundException("Account " + username + " is not assigned to any employee")
        );
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
