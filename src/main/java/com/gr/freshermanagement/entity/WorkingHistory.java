package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class WorkingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startTime;

    private LocalDate endTime;

    private WorkingStatus status;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Center center;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public enum WorkingStatus{
        EDUCATING, WORKING, TERMINATED, MANAGE
    }
}
