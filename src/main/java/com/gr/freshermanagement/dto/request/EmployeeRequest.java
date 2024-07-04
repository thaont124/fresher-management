package com.gr.freshermanagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String name;
    private LocalDate dob;
    private String address;
    private String phone;
    private String gender;
    private String email;
    private String fresherStatus;
}
