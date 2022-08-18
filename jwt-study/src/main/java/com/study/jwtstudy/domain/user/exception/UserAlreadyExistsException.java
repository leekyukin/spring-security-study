package com.study.jwtstudy.domain.user.exception;


import com.study.jwtstudy.global.error.exception.BusinessException;
import com.study.jwtstudy.global.error.exception.ErrorCode;

public class UserAlreadyExistsException extends BusinessException {

    public final static UserAlreadyExistsException EXCEPTION = new UserAlreadyExistsException();

    private UserAlreadyExistsException(){
        super(ErrorCode.USER_ALREADY_EXISTS);
    }
}
