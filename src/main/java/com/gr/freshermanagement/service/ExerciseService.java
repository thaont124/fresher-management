package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.excercise.ExerciseRequest;
import com.gr.freshermanagement.dto.request.excercise.GradeRequest;
import com.gr.freshermanagement.dto.request.excercise.RegisterExerciseRequest;
import com.gr.freshermanagement.dto.response.FresherExerciseResponse;
import com.gr.freshermanagement.dto.response.statistic.FresherScoreStatisticResponse;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.projection.CenterFresherCountProjection;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseService {
    void gradeExercise(GradeRequest gradeDTO, Employee mentor);

    List<FresherExerciseResponse> viewGrades(Long fresherId);

    void registerExercise(RegisterExerciseRequest request);

    List<FresherScoreStatisticResponse> getFresherScoreStats(LocalDate date, LocalDate endDate, Double minScore, Double maxScore) ;

    List<CenterFresherCountProjection> getFresherScoresByCenterAndDate(LocalDate date);

    void createExercise(ExerciseRequest requestDTO);
}
