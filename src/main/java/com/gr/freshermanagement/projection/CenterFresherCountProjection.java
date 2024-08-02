package com.gr.freshermanagement.projection;

import java.util.List;

public interface CenterFresherCountProjection {
    Long getCenterId();
    String getCenterName();
    List<FresherDetail> getFreshers();

    interface FresherDetail {
        Long getId();
        String getName();
        Double getScore();
    }
}