package com.gr.freshermanagement.repository;

import com.gr.freshermanagement.entity.FresherExercise;
import com.gr.freshermanagement.projection.CenterFresherCountProjection;
import com.gr.freshermanagement.projection.FresherScoreStatsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface FresherExerciseRepository extends JpaRepository<FresherExercise, Long> {
    @Query("select fe from FresherExercise fe " +
            "where fe.fresher.id = :fresherId")
    List<FresherExercise> findByFresherId(@Param("fresherId") Long fresherId);

    @Query("SELECT e.fresher.id as fresherId, e.fresher.name as fresherName, " +
            "COALESCE(AVG(e.mark), 0) as averageScore " +
            "FROM FresherExercise e " +
            "WHERE e.submitDate = :date " +
            "GROUP BY e.fresher.id, e.fresher.name " +
            "HAVING COUNT(e.id) >= 3")
    List<FresherScoreStatsProjection> findFresherScoresByDate(@Param("date") LocalDate date);

    @Query("SELECT w.center.id as centerId, w.center.name as centerName, " +
            "e.fresher.id as fresherId, e.fresher.name as fresherName, " +
            "COALESCE(AVG(e.mark), 0) as averageScore " +
            "FROM FresherExercise e " +
            "JOIN WorkingHistory w ON e.fresher.id = w.employee.id " +
            "WHERE e.submitDate = :date AND w.startTime <= :date AND (w.endTime IS NULL OR w.endTime >= :date) " +
            "GROUP BY w.center.id, w.center.name, e.fresher.id, e.fresher.name " +
            "HAVING COUNT(e.id) >= 3")
    List<CenterFresherCountProjection> findFresherScoresByCenterAndDate(LocalDate date);
}
