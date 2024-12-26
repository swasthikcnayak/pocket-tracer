package com.pocket.services.common.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class ValidationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        logger.error("constraint violation", ex);
        Map<String, String> errorMessages = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errorMessages.put(fieldName, errorMessage);
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        logger.error("Method argument exception", ex);
        Map<String, String> errorMessages = new HashMap<>();
        List<ObjectError> violations = ex.getAllErrors();
        for (ObjectError violation : violations) {
            String errorMessage = violation.getDefaultMessage();
            if(errorMessage!=null){
            String fieldName = errorMessage.split(" ")[0];
            errorMessages.put(fieldName, errorMessage);
        }
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessagenotReadableException(
            HttpMessageNotReadableException ex) {
        logger.error("Unreadable message exception", ex);
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("error", "INVALID JSON STRING");
        return new ResponseEntity<>(errorMessages, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}