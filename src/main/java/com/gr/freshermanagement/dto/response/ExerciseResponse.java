package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.entity.FresherExercise;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExerciseResponse {
    private Long id;

    private String name;

    private String exerciseCode;

    private String description;

    private LocalDate startDate;

    private LocalDate deadline;

    private String status;
}
