package com.example.jwttest.user.presentation.dto.request;

import com.example.jwttest.user.domain.User;
import com.example.jwttest.user.domain.type.Role;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String username;

    public User toEntity() {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
