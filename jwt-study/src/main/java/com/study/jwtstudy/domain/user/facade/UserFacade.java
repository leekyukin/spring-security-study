package com.study.jwtstudy.domain.user.facade;

import com.study.jwtstudy.domain.user.domain.User;
import com.study.jwtstudy.domain.user.domain.repository.UserRepository;
import com.study.jwtstudy.domain.user.exception.UserAlreadyExistsException;
import com.study.jwtstudy.domain.user.exception.UserNotFoundException;
import com.study.jwtstudy.global.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    public void validateSingUp(String email) {
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException.EXCEPTION;
        }
    }
}
