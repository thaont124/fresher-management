package com.gr.freshermanagement.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FresherScoreStatisticResponse {
    private Long fresherId;
    private String fresherName;
    private Double averageScore;
    private int getTotalExams;
    private List<ExerciseResultResponse> exercises;
}
