package com.gr.freshermanagement.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseResultResponse {
    private Long id;
    private Double mark;
    private LocalDateTime submitDate;
}
