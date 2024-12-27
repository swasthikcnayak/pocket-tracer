package com.pocket.services.budget.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BudgetServiceExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(BudgetServiceExceptionHandler.class);

    @ExceptionHandler(BudgetServiceException.class)
    public ResponseEntity<Map<String, String>> handleExpenseServiceException(BudgetServiceException ex) {
        logger.error("budget service exception " + ex.errorCode, ex);
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("error", ex.getMessage());
        errorMessages.put("code", ex.errorCode);
        return new ResponseEntity<>(errorMessages, ex.status != null ? ex.status : HttpStatus.BAD_REQUEST);
    }
}
