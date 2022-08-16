package com.study.jwtstudy.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public boolean hasKeyBlackList(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
