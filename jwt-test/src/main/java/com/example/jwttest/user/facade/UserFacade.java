package com.example.jwttest.user.facade;

import com.example.jwttest.user.domain.User;
import com.example.jwttest.user.domain.repository.UserRepository;
import com.example.jwttest.user.exception.IncorrectPasswordException;
import com.example.jwttest.user.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserFacade {

    private final UserRepository userRepository;

    public UserFacade (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkUserExists(String username) {
        log.info("existsByUsername : {}", userRepository.existsByUsername(username));
        if(userRepository.existsByUsername(username))
            throw UserAlreadyExistsException.EXCEPTION;
    }

    public User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(user.getUsername());
    }

    public void checkPassword(User user, String password) {
        if(!user.getPassword().equals(password))
            throw IncorrectPasswordException.EXCEPTION;

    }
}
