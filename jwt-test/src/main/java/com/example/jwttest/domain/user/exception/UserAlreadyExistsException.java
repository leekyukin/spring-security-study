package com.example.jwttest.user.exception;

import com.example.jwttest.global.error.exception.ErrorCode;
import com.example.jwttest.global.error.exception.JwtException;

public class UserAlreadyExistsException extends JwtException {

    public final static UserAlreadyExistsException EXCEPTION = new UserAlreadyExistsException();

    private UserAlreadyExistsException() {
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
