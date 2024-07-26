package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;

    @Override
    public void sendOtpMessage(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        javaMailSender.send(msg);
    }
    @Override
    @Async
    public void sendOtpHTMLMessage(String to, String subject, String otp) throws MessagingException, IOException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);
        helper.setSubject(subject);

        // Read the HTML template into a String variable
        String htmlTemplate = new String(Files.readAllBytes(Paths.get(new ClassPathResource("template/otp-mail.html").getURI())));

        // Replace placeholders in the HTML template with dynamic values
        htmlTemplate = htmlTemplate.replace("${OTP}", otp);

        // Set the email's content to be the HTML template
        msg.setContent(htmlTemplate, "text/html; charset=utf-8");
        javaMailSender.send(msg);
    }
}
