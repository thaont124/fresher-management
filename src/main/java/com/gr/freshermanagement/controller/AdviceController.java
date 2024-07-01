package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ErrorResponse;
import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import com.gr.freshermanagement.exception.base.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AdviceController {
    private final MessageSource messageSource;
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public  ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        String errorDetails = new String("s");
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse errorDetails = new ErrorResponse("FORBIDDEN", ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExistUsernameException.class)
    public ResponseEntity<ResponseGeneral<String>> handleExistUsernameException(ExistUsernameException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(UsernamePasswordIncorrectException.class)
    public ResponseEntity<ResponseGeneral<String>> handleUsernamePasswordIncorrectException(UsernamePasswordIncorrectException ex) {
        return new ResponseEntity<>(ResponseGeneral.of(400, "Bad Request", ex.getMes()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse("NOT_FOUND", "No handler found for " + request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
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