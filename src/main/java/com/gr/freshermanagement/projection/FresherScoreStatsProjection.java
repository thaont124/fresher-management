package com.gr.freshermanagement.projection;

import java.time.LocalDate;

public interface FresherScoreStatsProjection{
    Long getFresherId();
    String getFresherName();
    Double getAverageScore();
}
