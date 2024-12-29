package com.pocket.services.expense.exceptions;

import org.springframework.http.HttpStatus;

public class ExpenseServiceException extends RuntimeException {
    String errorCode;
    HttpStatus status;

    public ExpenseServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = null;
    }

    public ExpenseServiceException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
