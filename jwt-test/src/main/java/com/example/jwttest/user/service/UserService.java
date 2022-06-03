package com.example.jwttest.user.service;

import com.example.jwttest.user.domain.User;
import com.example.jwttest.user.domain.repository.UserRepository;
import com.example.jwttest.user.facade.UserFacade;
import com.example.jwttest.user.presentation.dto.request.CreateUserRequestDto;
import com.example.jwttest.user.presentation.dto.request.LoginRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserFacade userFacade;

    public UserService (UserRepository userRepository, UserFacade userFacade) {
        this.userRepository = userRepository;
        this.userFacade = userFacade;
    }

    @Transactional
    public void signup(CreateUserRequestDto request) {
        userFacade.checkUserExists(request.getUsername());
        userRepository.save(request.toEntity());
    }

    @Transactional(readOnly = true)
    public User getProfile() {
        return userFacade.getCurrentUser();
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername());
        userFacade.checkPassword(user, request.getPassword());

    }
}
