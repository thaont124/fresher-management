package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.excercise.GradeRequest;
import com.gr.freshermanagement.dto.request.excercise.RegisterExerciseRequest;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.Exercise;
import com.gr.freshermanagement.entity.FresherExercise;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.repository.ExerciseRepository;
import com.gr.freshermanagement.repository.FresherExerciseRepository;
import com.gr.freshermanagement.service.ExerciseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final FresherExerciseRepository fresherExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public void registerExercise(RegisterExerciseRequest request) {
        Employee fresher = employeeRepository.findById(request.getFresherId())
                .orElseThrow(() -> new NotFoundException("Fresher not found"));
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new NotFoundException("Exercise not found"));

        FresherExercise fresherExercise = new FresherExercise();
        fresherExercise.setFresher(fresher);
        fresherExercise.setExercise(exercise);
        fresherExercise.setSubmitDate(LocalDateTime.now());
        fresherExerciseRepository.save(fresherExercise);
    }

    @Transactional
    public void gradeExercise(GradeRequest request) {
        FresherExercise fresherExercise = fresherExerciseRepository.findById(request.getFresherExerciseId())
                .orElseThrow(() -> new NotFoundException("FresherExercise not found"));

        fresherExercise.setMark(request.getMark());
        fresherExerciseRepository.save(fresherExercise);
    }

    public List<FresherExercise> viewGrades(Long fresherId) {
        return fresherExerciseRepository.findByFresherId(fresherId);
    }
}
