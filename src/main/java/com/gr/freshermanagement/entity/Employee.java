package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String employeeCode;

    private LocalDate dob;

    private String address;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String email;

    @OneToOne(mappedBy = "employee")
    private Account account;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @PrePersist
    protected void onCreate() {
        if (this.employeeCode == null || this.employeeCode.isEmpty()) {
            this.employeeCode = generateEmployeeCode();
        }
    }
    private String generateEmployeeCode() {
        StringBuilder baseCode = new StringBuilder(LocalDate.now().getYear() + String.valueOf(this.id));
        while (baseCode.length() < 12) {
            baseCode.insert(0, "0");
        }
        return baseCode.toString();
    }
}
