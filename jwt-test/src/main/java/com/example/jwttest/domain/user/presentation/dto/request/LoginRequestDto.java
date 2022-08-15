package com.example.jwttest.user.presentation.dto.request;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {

    @NotNull
    private String username;
    @NotNull
    private String password;
}
