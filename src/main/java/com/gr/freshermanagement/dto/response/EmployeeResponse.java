package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Department;
import com.gr.freshermanagement.entity.EmployeeStatus;
import com.gr.freshermanagement.entity.Gender;
import jakarta.persistence.*;

import java.time.LocalDate;

public class EmployeeResponse {
    private String name;

    private LocalDate dob;

    private String address;

    private String phone;

    private String gender;

    private String email;

    private String username;

    private String status;
}
