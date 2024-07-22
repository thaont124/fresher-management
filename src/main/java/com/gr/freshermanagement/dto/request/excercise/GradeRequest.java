package com.gr.freshermanagement.dto.request.excercise;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GradeRequest {
    private Long fresherExerciseId;
    private float mark;
    LocalDateTime submitDate;
}
