package com.pocket.services.user.exceptions;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {
    String errorCode;
    HttpStatus status;

    public UserServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.status = null;
    }

    public UserServiceException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
