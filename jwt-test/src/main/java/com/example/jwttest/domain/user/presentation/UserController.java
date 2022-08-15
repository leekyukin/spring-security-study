package com.example.jwttest.user.presentation;

import com.example.jwttest.user.domain.User;
import com.example.jwttest.user.presentation.dto.request.CreateUserRequestDto;
import com.example.jwttest.user.presentation.dto.request.LoginRequestDto;
import com.example.jwttest.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public void sign(
            @RequestBody CreateUserRequestDto request
    ) {
        userService.signup(request);
    }

    @GetMapping("/profile")
    public User getProfile() {
        return userService.getProfile();
    }

    @PostMapping("/login")
    public void loginUser(
            @RequestBody LoginRequestDto request
    ) {
        userService.login(request);
    }
}
