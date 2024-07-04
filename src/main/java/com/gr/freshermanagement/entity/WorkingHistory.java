package com.gr.freshermanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Facility facility;

    @ManyToOne
    private Employee employee;

    public enum WorkingStatus{
        EDUCATING, WORKING, TERMINATED
    }
}
