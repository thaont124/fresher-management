package com.gr.freshermanagement.dto.response.statistic;

import com.gr.freshermanagement.dto.response.exercise.ScoreResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterFresherCountResponse {
    private Long centerId;
    private String centerName;
    private List<ScoreResponse> freshers;
}