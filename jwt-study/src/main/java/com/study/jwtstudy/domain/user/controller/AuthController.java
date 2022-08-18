package com.study.jwtstudy.domain.user.controller;

import com.study.jwtstudy.domain.user.controller.dto.LoginRequestDto;
import com.study.jwtstudy.domain.user.controller.dto.TokenResponseDto;
import com.study.jwtstudy.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponseDto login(
            @RequestBody @Valid LoginRequestDto request
    ) {
        return authService.login(request);
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        log.info(request.getHeader("Authentiction"));
        authService.logout(request.getHeader("ACCESS-TOKEN"));
    }
}
