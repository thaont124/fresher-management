package com.gr.freshermanagement.entity;

import com.gr.freshermanagement.commons.EmployeeStatus;
import com.gr.freshermanagement.commons.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    private String position;

    private LocalDateTime modifiedTime;

    @OneToOne(mappedBy = "employee")
    private Account account;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployeeLanguage> employeeLanguages;

    @PostPersist
    @PostUpdate
    protected void onPostPersist() {
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
