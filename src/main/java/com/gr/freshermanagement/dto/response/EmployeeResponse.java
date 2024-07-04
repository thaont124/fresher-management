package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Department;
import com.gr.freshermanagement.entity.EmployeeStatus;
import com.gr.freshermanagement.entity.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String name;
    private String employeeCode;
    private LocalDate dob;
    private String address;
    private String phone;
    private String gender;
    private String email;
    private String department;
    private String status;
    private String fresherStatus;


}

