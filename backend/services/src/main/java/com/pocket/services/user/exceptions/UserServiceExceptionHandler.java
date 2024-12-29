package com.pocket.services.user.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserServiceExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(UserServiceExceptionHandler.class);

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<Map<String, String>> handleUserServiceException(UserServiceException ex) {
        logger.error("user service violation " + ex.errorCode, ex);
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("error", ex.getMessage());
        errorMessages.put("code", ex.errorCode);
        return new ResponseEntity<>(errorMessages, ex.status != null ? ex.status : HttpStatus.BAD_REQUEST);
    }
}