package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class Fresher extends Employee{

    @Enumerated(EnumType.STRING)
    private FresherStatus fresherStatus;

    public Fresher(String name, String address, String phone, String email, Gender gender,
                   LocalDate dob, Department department, EmployeeStatus employeeStatus) {
        this.setName(name);
        this.setAddress(address);
        this.setPhone(phone);
        this.setEmail(email);
        this.setGender(gender);
        this.setDob(dob);
        this.setDepartment(department);
        this.setStatus(employeeStatus);
        this.fresherStatus = FresherStatus.EDUCATING;
    }


    public void generateFresherCode(){
        this.setEmployeeCode("FSH" + this.getId());
    }
}
