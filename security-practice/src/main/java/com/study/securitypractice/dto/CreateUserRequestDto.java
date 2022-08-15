package com.study.securitypractice.dto;

import com.study.securitypractice.user.domain.Authority;
import com.study.securitypractice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    private String email;
    private String password;
    private String name;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .authority(Authority.USER)
                .build();
    }
}
