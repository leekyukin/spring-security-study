package com.study.jwtstudy.domain.user.controller.dto;

import com.study.jwtstudy.domain.user.domain.User;
import com.study.jwtstudy.domain.user.domain.type.Role;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

@Getter
public class CreateUserRequestDto {

    @NotNull(message = "이름 빔")
    private String name;

    @NotNull(message = "이메일 빔")
    private String email;

    @NotNull(message = "비밀번호 빔")
    private String password;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_USER)
                .build();
    }
}
