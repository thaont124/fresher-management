package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.FresherExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FresherExerciseRepository extends JpaRepository<FresherExercise, Long> {
    @Query("select fe from FresherExercise fe " +
            "where fe.fresher.id = :fresherId")
    List<FresherExercise> findByFresherId(@Param("fresherId") Long fresherId);
}
