package com.example.jwttest.global.error.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public JwtException (ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public JwtException (ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
