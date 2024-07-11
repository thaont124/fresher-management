package com.gr.freshermanagement.dto.request.employee;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NewEmployeeRequest {
    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotEmpty(message = "Address is required")
    private String address;

    @NotEmpty(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotEmpty(message = "Gender is required")
    private String gender;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Languages list is required")
    @NotEmpty(message = "Languages list cannot be empty")
    @Size(min = 1, message = "Languages list must contain at least one language")
    private List<String> languages;

    @NotEmpty(message = "Position is required")
    private String position;
}