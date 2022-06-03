package com.example.jwttest.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "서버에 오류 발생"),
    BAD_REQUEST(400, "잘못된 요청"),

    USER_NOT_FOUND(404, "존재하지 않는 사용자"),
    USER_ALREADY_EXISTS(422, "이미 존재하는 사용자"),
    INCORRECT_PASSWORD(401, "틀린 비밀번호")
    ;

    private final int status;
    private final String message;
}
