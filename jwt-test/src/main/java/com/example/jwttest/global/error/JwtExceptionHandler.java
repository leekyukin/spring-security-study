package com.example.jwttest.global.error;

import com.example.jwttest.global.error.exception.ErrorCode;
import com.example.jwttest.global.error.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(JwtException.class)
    public ErrorResponseDto handleJwtException(JwtException e, HttpServletRequest request) {
        log.error(
                "errorCode : {}, url : {}, message : {}",
                e.getErrorCode(), request.getRequestURI(), e.getMessage()
                );
        return ErrorResponseDto.of (
                e.getErrorCode().getStatus(),
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponseDto handleException(Exception e, HttpServletRequest request) {
        log.error(
                "url : {}, message : {}",
                request.getRequestURI(), e.getMessage()
        );
        return ErrorResponseDto.of (
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );
    }
}
