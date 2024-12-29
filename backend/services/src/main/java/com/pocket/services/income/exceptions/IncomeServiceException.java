package com.pocket.services.income.exceptions;

import org.springframework.http.HttpStatus;

public class IncomeServiceException extends RuntimeException {
    String errorCode;
    HttpStatus status;

    public IncomeServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = null;
    }

    public IncomeServiceException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
