package com.study.securitypractice.controller;

import com.study.securitypractice.dto.CreateUserRequestDto;
import com.study.securitypractice.dto.UserLoginDto;
import com.study.securitypractice.facade.UserFacade;
import com.study.securitypractice.security.jwt.JwtProvider;
import com.study.securitypractice.service.UserService;
import com.study.securitypractice.user.domain.User;
import com.study.securitypractice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public void join(
            @RequestBody CreateUserRequestDto request
    ) {
        userRepository.save(request.toEntity(passwordEncoder));
    }

    @PostMapping("/login")
    public String login(
            @RequestBody UserLoginDto request, HttpServletResponse response
    ) {
        return userService.login(request, response);
    }
}
