package com.example.orderprocessingservice.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException{
    private final HttpStatus status;
    private final String code;

    protected BaseException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
