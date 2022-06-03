package com.example.jwttest.global.jwt;

import com.example.jwttest.user.domain.User;
import com.example.jwttest.user.domain.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = null;
        try {
            // cookie 에서 token 을 가져옴
            // Arrays.stream(arr[배열값]) 배열 스트림 생성
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } catch (Exception ignored) {
        }
        if (token != null) {
            try {
                Authentication authentication = getUsernamePasswordAuthenticationToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
                cookie.setMaxAge(0); // 0 = 쿠키 만료 시간 = ∞
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로 User 를 찾아서 UsernamePasswordAuthenticationToken 를 만들어서 반환한다.
     * User 가 없다면 null
     */
    private Authentication getUsernamePasswordAuthenticationToken(String token) {
        String name = JwtUtils.getUsername(token);
        if (name != null) {
            User user = userRepository.findByUsername(name);
            return new UsernamePasswordAuthenticationToken(
                    user,   // principle
                    null,
                    user.getAuthorities()   // authorities
            );
        }
        return null; // 유저가 없으면 null
    }

}
