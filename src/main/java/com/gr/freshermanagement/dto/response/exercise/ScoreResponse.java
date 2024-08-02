package com.gr.freshermanagement.dto.response.exercise;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreResponse {
    private Long fresherId;
    private String fresherName;
    private Double avgScore;
}
