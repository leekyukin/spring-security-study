package com.study.securitypractice.service;

import com.study.securitypractice.dto.UserLoginDto;
import com.study.securitypractice.facade.UserFacade;
import com.study.securitypractice.security.jwt.JwtProvider;
import com.study.securitypractice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String login(UserLoginDto request, HttpServletResponse response) {
        User user = userFacade.findUserByEmail(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalStateException("잘못된 비밀번호입니다.");
        }

        String token = jwtProvider.createToken(user.getEmail(), user.getAuthority());
        response.setHeader("X-AUTH-TOKEN", token);

        Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return user.getName();
    }
}
