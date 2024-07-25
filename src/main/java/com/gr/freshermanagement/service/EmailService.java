package com.gr.freshermanagement.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOtpMessage(String to, String subject, String message) throws MessagingException;
}
