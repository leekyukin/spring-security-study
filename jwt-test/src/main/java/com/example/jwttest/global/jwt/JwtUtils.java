package com.example.jwttest.global.jwt;


import com.example.jwttest.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.springframework.data.util.Pair;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    public static String getUsername(String token) {
        // jwt 에서 name 을 찾는다.
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // username
    }

    /**
     * user 로 토큰 생성
     * HEADER : alg, kid
     * PAYLOAD : sub, iat, exp
     * SIGNATURE : JwtKey.getRandomKey 로 구한 Secret Key 로 HS512 해시
     *
     * @param user 유저
     * @return jwt token
     */
    public static String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        // JWT Token
        return Jwts.builder()
                .setClaims(claims)  // 정보 저장
                .setIssuedAt(now)   // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME))
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst())    // kid
                .signWith(key.getSecond())  // signature
                .compact();
    }
}
