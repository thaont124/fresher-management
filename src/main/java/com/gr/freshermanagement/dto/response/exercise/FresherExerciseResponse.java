package com.gr.freshermanagement.dto.response.exercise;

import com.gr.freshermanagement.dto.response.employee.EmployeeResponse;
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