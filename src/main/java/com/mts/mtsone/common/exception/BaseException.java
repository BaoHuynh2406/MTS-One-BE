package com.mts.mtsone.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final String message;
    private final HttpStatus status;

    public BaseException(String code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public BaseException(String code, String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.status = status;
    }
} 