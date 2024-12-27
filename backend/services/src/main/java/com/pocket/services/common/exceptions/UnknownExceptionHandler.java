package com.pocket.services.common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UnknownExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    @ExceptionHandler(UnhandledException.class)
    public ResponseEntity<Map<String, String>> handleUnknownException(UnhandledException ex) {
        logger.error("uhandled exception", ex);
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("error", "Unknown exception occurred");
        return new ResponseEntity<>(errorMessages,
                ex.getStatus() != null ? ex.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}