package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.entity.*;
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
    private String avtUrl;

    public EmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.employeeCode = employee.getEmployeeCode();
        this.dob = employee.getDob();
        this.address = employee.getAddress();
        this.phone = employee.getPhone();
        this.gender = String.valueOf(employee.getGender());
        this.email = employee.getEmail();
        this.status = String.valueOf(employee.getStatus());
    }
}

