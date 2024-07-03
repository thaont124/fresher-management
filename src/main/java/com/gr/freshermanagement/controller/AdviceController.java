package com.gr.freshermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import com.gr.freshermanagement.exception.department.DepartmentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
    private static final Logger logger = LoggerFactory.getLogger(AdviceController.class);
    private final MessageSource messageSource;
    private ObjectMapper objectMapper;

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseGeneral<String>> handleExpiredTokenException(NoResourceFoundException ex) {
        logger.error("Error: ", ex.getMessage());
        return new ResponseEntity<>(ResponseGeneral.of(404, "Not Found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ExistUsernameException.class)
    public ResponseEntity<ResponseGeneral<String>> handleExistUsernameException(ExistUsernameException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UsernamePasswordIncorrectException.class)
    public ResponseEntity<ResponseGeneral<String>> handleUsernamePasswordIncorrectException(UsernamePasswordIncorrectException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ResponseGeneral<String>> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
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