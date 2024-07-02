package com.gr.freshermanagement.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Mentor extends Employee {

    private String mentorStatus;
}