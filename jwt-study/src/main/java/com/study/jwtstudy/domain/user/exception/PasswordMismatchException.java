package com.study.jwtstudy.domain.user.exception;


import com.study.jwtstudy.global.error.exception.BusinessException;
import com.study.jwtstudy.global.error.exception.ErrorCode;

public class PasswordMismatchException extends BusinessException {

    public final static PasswordMismatchException EXCEPTION = new PasswordMismatchException();

    private PasswordMismatchException(){
        super(ErrorCode.PASSWORD_MISMATCH);
    }
}
