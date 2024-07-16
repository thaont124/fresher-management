package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.excercise.GradeRequest;
import com.gr.freshermanagement.dto.request.excercise.RegisterExerciseRequest;
import com.gr.freshermanagement.dto.response.FresherExerciseResponse;
import com.gr.freshermanagement.projection.CenterFresherCountProjection;
import com.gr.freshermanagement.projection.FresherScoreStatsProjection;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseService {
    void gradeExercise(GradeRequest gradeDTO);

    List<FresherExerciseResponse> viewGrades(Long fresherId);

    void registerExercise(RegisterExerciseRequest request);

    List<FresherScoreStatsProjection> getFresherScoreStats(LocalDate date) ;

    List<CenterFresherCountProjection> getFresherScoresByCenterAndDate(LocalDate date);
}
