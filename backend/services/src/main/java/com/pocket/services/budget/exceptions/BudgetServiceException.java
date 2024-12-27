package com.pocket.services.budget.exceptions;

import org.springframework.http.HttpStatus;

public class BudgetServiceException extends RuntimeException {
    String errorCode;
    HttpStatus status;
    public BudgetServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = null;
    }

    public BudgetServiceException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
