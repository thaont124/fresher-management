package com.gr.freshermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import com.gr.freshermanagement.exception.base.NotFoundException;
import com.gr.freshermanagement.exception.department.DepartmentNotFoundException;
import com.gr.freshermanagement.exception.employee.EmployeeInAnotherCenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ResponseGeneral<String>> handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(EmployeeInAnotherCenter.class)
    public ResponseEntity<ResponseGeneral<String>> handleEmployeeInAnotherCenter(EmployeeInAnotherCenter ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseGeneral<String>> handleExpiredTokenException(NoResourceFoundException ex) {
        logger.error("Error: ", ex.getMessage());
        return new ResponseEntity<>(ResponseGeneral.of(404, "Not Found", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseGeneral<String>> handleNotFoundException(NotFoundException ex) {
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


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseGeneral<String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(405, "Method Not Allowed", ex.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);

    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ResponseGeneral<String>> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(406, "Not Acceptable", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);

    }
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseGeneral<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(403, "Don't have permission to access this resource", null), HttpStatus.FORBIDDEN );

    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseGeneral<String>> handleAuthorizationDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(403, ex.getMessage(), null), HttpStatus.FORBIDDEN );

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseGeneral<String>> handleHttpMediaTypeNotAcceptableException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, ex.getFieldError().getDefaultMessage(), null), HttpStatus.BAD_REQUEST);

    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResponseGeneral<String>> handleException(Exception ex) {
//        return new ResponseEntity<>(ResponseGeneral.of(500, "Internal Server Error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//
//    }
}