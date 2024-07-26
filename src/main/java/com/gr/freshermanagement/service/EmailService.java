package com.gr.freshermanagement.service;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    void sendOtpMessage(String to, String subject, String message) throws MessagingException;

    void sendOtpHTMLMessage(String to, String subject, String otp) throws MessagingException, IOException;
}
