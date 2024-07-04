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


    public void generateFresherCode(){
        this.setEmployeeCode("FSH" + this.getId());
    }
}
