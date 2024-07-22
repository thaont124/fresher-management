package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.excercise.ExerciseRequest;
import com.gr.freshermanagement.dto.request.excercise.GradeRequest;
import com.gr.freshermanagement.dto.request.excercise.RegisterExerciseRequest;
import com.gr.freshermanagement.dto.response.FresherExerciseResponse;
import com.gr.freshermanagement.dto.response.statistic.ExerciseResultResponse;
import com.gr.freshermanagement.dto.response.statistic.FresherScoreStatisticResponse;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.Exercise;
import com.gr.freshermanagement.entity.FresherExercise;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.projection.CenterFresherCountProjection;
import com.gr.freshermanagement.projection.FresherScoreStatsProjection;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.repository.ExerciseRepository;
import com.gr.freshermanagement.repository.FresherExerciseRepository;
import com.gr.freshermanagement.service.ExerciseService;
import com.gr.freshermanagement.utils.MapperUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        fresherExerciseRepository.save(fresherExercise);
    }

    @Transactional
    public void gradeExercise(GradeRequest request, Employee mentor) {
        FresherExercise fresherExercise = fresherExerciseRepository.findById(request.getFresherExerciseId())
                .orElseThrow(() -> new NotFoundException("FresherExercise not found"));

        fresherExercise.setMark(request.getMark());
        fresherExercise.setSubmitDate(request.getSubmitDate());
        fresherExercise.setMentor(mentor);
        fresherExerciseRepository.save(fresherExercise);
    }

    public List<FresherExerciseResponse> viewGrades(Long fresherId) {
        List<FresherExercise> fresherExercises = fresherExerciseRepository.findByFresherId(fresherId);


        return MapperUtils.toDTOs(fresherExercises, FresherExerciseResponse.class);
    }

    public List<FresherScoreStatisticResponse> getFresherScoreStats(LocalDate startDate, LocalDate endDate,
                                                                    Double minScore, Double maxScore) {
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();
        List<FresherScoreStatsProjection> projections =
                fresherExerciseRepository.findFresherScoresByDateRange(startTime, endTime, minScore, maxScore);

        return projections.stream()
                .map(projection -> {
                    List<FresherExercise> exercises = fresherExerciseRepository.findExercisesByFresher(projection.getFresherId());
                    List<ExerciseResultResponse> exerciseResponses = exercises.stream()
                            .map(exercise -> new ExerciseResultResponse(exercise.getId(), exercise.getMark(), exercise.getSubmitDate()))
                            .collect(Collectors.toList());
                    return new FresherScoreStatisticResponse(
                            projection.getFresherId(),
                            projection.getFresherName(),
                            projection.getAverageScore(),
                            projection.getTotalExams(),
                            exerciseResponses
                    );
                })
                .sorted(Comparator.comparing(FresherScoreStatisticResponse::getAverageScore).reversed())
                .collect(Collectors.toList());
    }



    @Override
    public List<CenterFresherCountProjection> getFresherScoresByCenterAndDate(LocalDate date) {
        return fresherExerciseRepository.findFresherScoresByCenterAndDate(date);
    }

    @Override
    public void createExercise(ExerciseRequest requestDTO) {
        Exercise exercise = new Exercise();
        exercise.setName(requestDTO.getName());
        exercise.setDescription(requestDTO.getDescription());
        exercise.setStartDate(requestDTO.getStartDate());
        exercise.setDeadline(requestDTO.getDeadline());

        // Additional logic if needed

        exerciseRepository.save(exercise);
    }
}
