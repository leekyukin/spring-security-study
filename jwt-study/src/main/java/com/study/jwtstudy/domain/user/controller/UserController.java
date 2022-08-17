package com.study.jwtstudy.domain.user.controller;

import com.study.jwtstudy.domain.user.controller.dto.CreateUserRequestDto;
import com.study.jwtstudy.domain.user.domain.User;
import com.study.jwtstudy.domain.user.domain.repository.UserRepository;
import com.study.jwtstudy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    public void signUp(
            @RequestBody @Valid CreateUserRequestDto request
    ) {
        userService.signUp(request);
    }

    @GetMapping("/user-list")
    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
