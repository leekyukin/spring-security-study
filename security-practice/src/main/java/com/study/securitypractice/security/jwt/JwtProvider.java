package com.study.securitypractice.security.jwt;

import com.study.securitypractice.user.domain.Authority;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final String secretKey = "secret";

    private final long tokenValidTime = 1000L * 60 * 60;;

    private final UserDetailsService userDetailsService;

    // JWT 토큰 생성
    public String createToken(String userPk, Authority authority) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보
        claims.put("roles", authority); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)  // 정보 저장
                .setIssuedAt(now)   // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime))    // 망료시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용알 암호화 알고리즘과 signature 에 들어갈 secret 값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request 의 Header 에서 token 값을 가져옵니다. "X-AUTH_TOKEN" : "TOKEN 값"
    public String resolveToken(HttpServletRequest request) {
        String token = null;
        Cookie cookie = WebUtils.getCookie(request, "X-AUTH=TOKEN");
        if (cookie != null) token = cookie.getValue();
        return token;
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
