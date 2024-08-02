package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.response.statistic.CenterFresherCountResponse;
import com.gr.freshermanagement.dto.response.statistic.FresherScoreStatisticResponse;
import com.gr.freshermanagement.projection.CenterFresherCountProjection;
import com.gr.freshermanagement.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final ExerciseService exerciseService;
    @GetMapping("/fresher/score")
    public ResponseEntity<?> getFresherScoreStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Double minScore,
            @RequestParam Double maxScore) {
        if (endDate == null) {
            endDate = LocalDate.now();
            startDate = LocalDate.now().minusDays(30);
        }

        List<FresherScoreStatisticResponse> stats = exerciseService.getFresherScoreStats(startDate, endDate, minScore, maxScore);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Get statistic success", stats));

    }

    @GetMapping("/fresher/center")
    public ResponseEntity<?> getFresherScoresByCenterAndDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<CenterFresherCountResponse> fresherScores = exerciseService.getFresherScoresByCenterAndDate(date);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Get statistic success", fresherScores));

    }
}
