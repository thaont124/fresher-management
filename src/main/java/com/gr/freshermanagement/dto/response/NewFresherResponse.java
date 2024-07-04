package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.entity.Fresher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewFresherResponse {
    private String name;

    private LocalDate dob;

    private String address;

    private String phone;

    private String gender;

    private String email;

    private String username;

    private String firstPassword;

    private String status;

    public NewFresherResponse(Fresher employee, String username, String firstPassword) {
        this.name = employee.getName();
        this.dob = employee.getDob();
        this.address = employee.getAddress();
        this.phone = employee.getPhone();
        this.gender = employee.getGender().toString(); // Assuming gender is an Enum
        this.email = employee.getEmail();
        this.username = username;
        this.firstPassword = firstPassword;
        this.status = employee.getStatus().toString(); // Assuming status is an Enum
    }
}