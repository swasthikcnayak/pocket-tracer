package com.pocket.services.common.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UnhandledException extends RuntimeException {
    String errorCode;
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public UnhandledException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
