package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
    private final MessageSource messageSource;

    @ExceptionHandler(ExistUsernameException.class)
    public ResponseEntity<ResponseGeneral<String>> handleExistUsernameException(ExistUsernameException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UsernamePasswordIncorrectException.class)
    public ResponseEntity<ResponseGeneral<String>> handleUsernamePasswordIncorrectException(UsernamePasswordIncorrectException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }


    private String getMessage(String code, Locale locale) {
        try {
            return messageSource.getMessage(code, null, locale);
        } catch (Exception ex) {
            return code;
        }
    }

    private String getMessageParamsKey(String key) {
        return "%" + key + "%";
    }
}