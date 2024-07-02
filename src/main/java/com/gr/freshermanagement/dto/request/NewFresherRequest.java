package com.gr.freshermanagement.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NewFresherRequest {
    private String name;

    private LocalDate dob;

    private String address;

    private String phone;

    private String gender;

    private String email;

    private Long departmentId;

}
