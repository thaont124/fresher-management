package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
