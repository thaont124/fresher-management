package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.excercise.ExerciseRequest;
import com.gr.freshermanagement.dto.request.excercise.GradeRequest;
import com.gr.freshermanagement.dto.request.excercise.RegisterExerciseRequest;
import com.gr.freshermanagement.dto.response.exercise.FresherExerciseResponse;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<?> createExercise(@RequestBody ExerciseRequest requestDTO) {
        try {
            exerciseService.createExercise(requestDTO);
            return ResponseEntity.ok(ResponseGeneral.of(200, "Registered successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseGeneral.of(500, "Failed to register", null));
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerExercise(@RequestBody @Valid RegisterExerciseRequest request) {
        exerciseService.registerExercise(request);
        return ResponseEntity.ok(ResponseGeneral.of(200, "Registered successfully", null));
    }

    @PostMapping("/grade")
    public ResponseEntity<?> gradeExercise(@RequestBody @Valid GradeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Employee mentor = employeeService.findEmployeeByUsername(username);
        exerciseService.gradeExercise(request, mentor);
        return ResponseEntity.ok(ResponseGeneral.of(200, "Graded successfully", null));
    }

    @GetMapping("/viewGrades")
    public ResponseEntity<?> viewGrades() {
        // Lấy thông tin người dùng hiện tại từ Security Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Employee fresher = employeeService.findFresherByAccountUsername(username)
                .orElseThrow(() -> new NotFoundException("Fresher not found with username: " + username));

        List<FresherExerciseResponse> grades = exerciseService.viewGrades(fresher.getId());
        return ResponseEntity.ok(ResponseGeneral.of(200, "view success",grades));
    }
}
