package com.gr.freshermanagement.service;

public interface OtpService {
    int generateOTP(String key);

    int getOtp(String key);

    void clearOTP(String key);
}
