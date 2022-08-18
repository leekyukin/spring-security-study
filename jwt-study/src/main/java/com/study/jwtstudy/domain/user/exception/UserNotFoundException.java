package com.study.jwtstudy.domain.user.exception;


import com.study.jwtstudy.global.error.exception.BusinessException;
import com.study.jwtstudy.global.error.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public final static UserNotFoundException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException(){
        super(ErrorCode.USER_NOT_FOUND);
    }
}
