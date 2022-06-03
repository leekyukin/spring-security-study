package com.example.jwttest.global.error;


import com.example.jwttest.global.error.exception.JwtException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {

    private final int status;
    private final String message;

    public static ErrorResponseDto of (int status, String message) {
        return ErrorResponseDto.builder()
                .status(status)
                .message(message)
                .build();
    }
}
