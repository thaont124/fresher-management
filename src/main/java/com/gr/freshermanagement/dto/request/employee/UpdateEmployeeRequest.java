package com.gr.freshermanagement.dto.request.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {
    private String name;
    private LocalDate dob;
    private String address;
    private String phone;
    private String gender;
    private String email;
    private String fresherStatus;
    private MultipartFile avt;
}
