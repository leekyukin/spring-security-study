package com.study.jwtstudy.domain.user.controller;

import com.study.jwtstudy.domain.user.controller.dto.LoginRequestDto;
import com.study.jwtstudy.domain.user.controller.dto.TokenResponseDto;
import com.study.jwtstudy.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponseDto login(
            @RequestBody @Valid LoginRequestDto request
    ) {
        return authService.login(request);
    }
}
