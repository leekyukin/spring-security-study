package com.study.jwtstudy.domain.user.facade;

import com.study.jwtstudy.domain.user.domain.User;
import com.study.jwtstudy.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않음"));
    }

    public void validateSingUp(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 유저");
        }
    }
}
