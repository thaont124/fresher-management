package com.gr.freshermanagement.projection;

import java.time.LocalDate;
import java.util.List;

public interface FresherScoreStatsProjection {
    Long getFresherId();
    String getFresherName();
    Double getAverageScore();
    Integer getTotalExams();
    List<FresherExerciseProjection> getExercises();
}
