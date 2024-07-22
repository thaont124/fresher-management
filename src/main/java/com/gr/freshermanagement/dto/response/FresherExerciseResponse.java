package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.ExerciseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FresherExerciseResponse {
    private LocalDateTime submitDate;
    private EmployeeResponse fresher;
    private ExerciseResponse exercise;
    private EmployeeResponse mentor;
    private float mark;

}