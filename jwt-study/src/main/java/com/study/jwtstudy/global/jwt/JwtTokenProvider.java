package com.study.jwtstudy.global.jwt;

import com.study.jwtstudy.global.config.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisService redisService;

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    @Value("${spring.security.jwt.blacklist.access-token}")
    private String blackListATPrefix;

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private String createToken(String email, List<String> roles, long time) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + time))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(String email, List<String> roles) {
        return createToken(email, roles, JwtProperties.ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(String email, List<String> roles) {
        return createToken(email, roles, JwtProperties.REFRESH_TOKEN_VALID_TIME);
    }


    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(JwtProperties.JWT_ACCESS);
    }

    public Claims extractAllClaims(String token) {
        try {
            if (!redisService.hasKeyBlackList(token))
                throw new RuntimeException("아직 로그인 안함");

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰 만료됨");
        }
    }

    public void logout(String email, String accessToken) {
        long expiredAccessToken = getExpiredTime(accessToken)
                .getTime() - new Date().getTime();

        redisService.setValues(blackListATPrefix+accessToken, email, Duration.ofMillis(expiredAccessToken));
        redisService.delete(email);

        redisService.setBlackList(accessToken, "ACCESS-TOKEN", expiredAccessToken);

    }

    private Date getExpiredTime(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getExpiration();
    }
}
