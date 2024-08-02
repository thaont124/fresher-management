package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.employee.EmployeeResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.service.impl.EmployeeServiceImpl;
import com.gr.freshermanagement.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MapperUtils mapperUtils;
    private Account account;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setUsername("ninh@gmail.com");
        account.setStatus(Account.AccountStatus.NEW_FRESHER); // Assuming you have an AccountStatus enum

        employee = new Employee();
        employee.setAccount(account);
        account.setEmployee(employee);
    }

    @Test
    public void updateEmployee_Success() {
        // Given
        String username = "ninh@gmail.com";
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .name("John Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone("1234567890")
                .gender("MALE")
                .position("Developer")
                .email("john.doe@example.com")
                .fresherStatus("NEW_FRESHER")
                .build();

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(account));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        EmployeeResponse response = employeeService.updateEmployee(username, updateEmployeeRequest);

        // Then
        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals(LocalDate.of(1990, 1, 1), response.getDob());
        assertEquals("123 Main St", response.getAddress());
        assertEquals("1234567890", response.getPhone());
        assertEquals("MALE", response.getGender());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("Developer", response.getPosition());

    }
}
