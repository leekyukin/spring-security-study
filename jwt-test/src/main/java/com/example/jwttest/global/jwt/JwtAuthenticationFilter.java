package com.example.jwttest.global.jwt;

import com.example.jwttest.user.domain.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter (
            AuthenticationManager authenticationManager
    ) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    /**
     * 로그인 인증 시도
     */
    @Override
    public Authentication attemptAuthentication (
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {

        // UsernamePasswordAuthenticationToken : 아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경한다.
        // 로그인할 때 입력한 username, password 를 가지고 UsernamePasswordAuthenticationToken 을 생성한다.
        // 또 마지막 권한 리스트 파라미터 자리에 ArrayList 를 생성해준다
        UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"),
                new ArrayList<>()   // authorities
        );
        // AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리하도록 한다.
        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * 인증에 성공했을 때 사용 /
     * JWT Token 을 생성해서 쿠키에 넣는다.
     */
    @Override
    protected void successfulAuthentication (
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        // 인증 성공한 Authentication 인 authResult 에서 principal 을 User 에 저장
        User user = (User) authResult.getPrincipal(); //
        String token = JwtUtils.createToken(user);  // 토큰 생성
        // 쿠키 생성
        // key, value 쌍인 토큰의 key(이름) : value(토큰) 넣은 쿠키 생성
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, token);
        cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);    // 쿠키 만료시간
        cookie.setPath("/"); // / 하위의 url 에서만 쿠키에 접근 가 (전부 접근 가능, default 값 : /)
        response.addCookie(cookie);
        response.sendRedirect("/");
    }

    @Override
    protected void unsuccessfulAuthentication (
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        response.sendRedirect("/login");
    }
}
