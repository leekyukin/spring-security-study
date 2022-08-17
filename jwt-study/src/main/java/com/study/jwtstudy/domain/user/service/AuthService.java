package com.study.jwtstudy.domain.user.service;

import com.study.jwtstudy.domain.user.controller.dto.LoginRequestDto;
import com.study.jwtstudy.domain.user.controller.dto.TokenResponseDto;
import com.study.jwtstudy.domain.user.domain.User;
import com.study.jwtstudy.domain.user.domain.repository.UserRepository;
import com.study.jwtstudy.domain.user.domain.type.Role;
import com.study.jwtstudy.domain.user.facade.UserFacade;
import com.study.jwtstudy.global.config.redis.RedisService;
import com.study.jwtstudy.global.jwt.JwtProperties;
import com.study.jwtstudy.global.jwt.JwtTokenProvider;
import com.study.jwtstudy.global.jwt.JwtValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponseDto login(LoginRequestDto request) {
        User user = userFacade.findUserByEmail(request.getEmail());

        user.matchedPassword(passwordEncoder, request.getPassword());

        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().name());

        final String accessToken = jwtTokenProvider.createAccessToken(request.getEmail(), roles);
        final String refreshToken = jwtTokenProvider.createRefreshToken(request.getEmail(), roles);
        redisService.setDataExpire(user.getEmail(), refreshToken, JwtProperties.REFRESH_TOKEN_VALID_TIME);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
