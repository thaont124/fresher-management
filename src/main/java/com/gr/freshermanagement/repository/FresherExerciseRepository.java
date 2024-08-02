package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.FresherExercise;
import com.gr.freshermanagement.projection.CenterFresherCountProjection;
import com.gr.freshermanagement.projection.FresherScoreStatsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FresherExerciseRepository extends JpaRepository<FresherExercise, Long> {
    @Query("select fe from FresherExercise fe " +
            "where fe.fresher.id = :fresherId")
    List<FresherExercise> findByFresherId(@Param("fresherId") Long fresherId);

    @Query("SELECT f.id AS fresherId, f.name AS fresherName, " +
            "CASE " +
            "   WHEN COUNT(e.id) >= 3 THEN COALESCE(AVG(e.mark), 0) " +
            "   ELSE 0 " +
            "END AS averageScore, " +
            "COUNT(e.id) AS totalExams " +
            "FROM Employee f " +
            "LEFT JOIN FresherExercise e ON f.id = e.fresher.id " +
            "AND e.submitDate BETWEEN :startDate AND :endDate " +
            "AND e.mentor IS NOT NULL " +
            "AND e.submitDate IS NOT NULL " +
            "GROUP BY f.id, f.name " +
            "HAVING (COUNT(e.id) >= 3 AND COALESCE(AVG(e.mark), 0) BETWEEN :minScore AND :maxScore) " +
            "OR COUNT(e.id) < 3 " +
            "ORDER BY averageScore DESC")
    List<FresherScoreStatsProjection> findFresherScoresByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("minScore") double minScore,
            @Param("maxScore") double maxScore);



    @Query("SELECT w.center.id as centerId, w.center.name as centerName, " +
            "e.fresher.id as fresherId, e.fresher.name as fresherName, " +
            "COALESCE(AVG(e.mark), 0) as averageScore " +
            "FROM FresherExercise e " +
            "JOIN WorkingHistory w ON e.fresher.id = w.employee.id " +
            "WHERE DATE(e.submitDate) = :date AND w.startTime <= :date AND (w.endTime IS NULL OR w.endTime >= :date) " +
            "GROUP BY w.center.id, w.center.name, e.fresher.id, e.fresher.name " +
            "HAVING (COUNT(e.id) >= 3) " +
            "OR COUNT(e.id) < 3 " +
            "ORDER BY averageScore DESC")
    List<CenterFresherCountProjection> findFresherScoresByCenterAndDate(@Param("date") LocalDate date);


    @Query("SELECT e FROM FresherExercise e WHERE e.fresher.id = :fresherId ")
    List<FresherExercise> findExercisesByFresher(@Param("fresherId") Long fresherId);


}
