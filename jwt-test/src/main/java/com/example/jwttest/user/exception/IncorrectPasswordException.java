package com.example.jwttest.user.exception;

import com.example.jwttest.global.error.exception.ErrorCode;
import com.example.jwttest.global.error.exception.JwtException;

public class IncorrectPasswordException extends JwtException {
    public static IncorrectPasswordException EXCEPTION = new IncorrectPasswordException();

    private IncorrectPasswordException() {
        super(ErrorCode.INCORRECT_PASSWORD);
    }

}
