package com.gr.freshermanagement.projection;

import java.time.LocalDateTime;

public interface FresherExerciseProjection {
    Long getId();
    Double getMark();
    LocalDateTime getSubmitDate();
}
