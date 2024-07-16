package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.projection.CenterFresherCountProjection;
import com.gr.freshermanagement.projection.FresherScoreStatsProjection;
import com.gr.freshermanagement.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
            @RequestParam("date") LocalDate date) {

        List<FresherScoreStatsProjection> stats = exerciseService.getFresherScoreStats(date);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/fresher/center")
    public ResponseEntity<?> getFresherScoresByCenterAndDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CenterFresherCountProjection> fresherScores = exerciseService.getFresherScoresByCenterAndDate(date);
        return ResponseEntity.ok(fresherScores);
    }
}
