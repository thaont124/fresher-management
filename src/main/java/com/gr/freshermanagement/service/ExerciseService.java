package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.excercise.GradeRequest;
import com.gr.freshermanagement.dto.request.excercise.RegisterExerciseRequest;
import com.gr.freshermanagement.entity.FresherExercise;

import java.util.List;

public interface ExerciseService {
    void gradeExercise(GradeRequest gradeDTO);

    List<FresherExercise> viewGrades(Long fresherId);

    void registerExercise(RegisterExerciseRequest request);
}
