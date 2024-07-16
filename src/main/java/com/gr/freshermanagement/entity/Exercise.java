package com.gr.freshermanagement.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String exerciseCode;

    private String description;

    private LocalDate startDate;

    private LocalDate deadline;

    private String status;

    @OneToMany(mappedBy = "exercise")
    private List<FresherExercise> fresherExercises;
}
