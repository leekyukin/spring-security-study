package com.study.jwtstudy.domain.user.service;

import com.study.jwtstudy.domain.user.controller.dto.CreateUserRequestDto;
import com.study.jwtstudy.domain.user.domain.repository.UserRepository;
import com.study.jwtstudy.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(CreateUserRequestDto request) {

        userFacade.validateSingUp(request.getEmail());

        userRepository.save(request.toEntity(passwordEncoder));
    }
}
