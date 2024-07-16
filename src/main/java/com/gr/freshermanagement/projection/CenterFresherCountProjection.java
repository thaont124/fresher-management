package com.gr.freshermanagement.projection;

public interface CenterFresherCountProjection {
    Long getCenterId();
    String getCenterName();
    Long getFresherId();
    String getFresherName();
    Double getAverageScore();
}