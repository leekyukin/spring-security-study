package com.example.jwttest.global.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

public class JwtKey {

    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", "sdfjklsjdflkjalkfjasjdfljas;ldfjaklsdjflka;jsdkl;fjk;lasjdfkl;jasdklfjaslk;djfla;jsdfl;kjasl;dkfjl;ksdjfl;asdjf;lajdsfl;ajdsfl;kjasd;",
            "key2", "jfklasjdklsdfsdfihjsdkfhjkshdfkjlsdjfl;jskdlfj;ljaskldfjalsjdfk;ljsak;lfjl;ksjdl;fkjjl;ksjdl;fkjjl;ksjdl;fkjjl;ksjdl;fkjjl;ksjdl;fkj",
            "key3", "jl;ksjdl;fkj989080jl;ksjdl;fkj888jl;ksjdl;fkjjl;ksjdl;fkjsdfsdfjl;ksjdl;fkjsdfsdfjl;ksjdl;fkjsdfsdfjl;ksjdl;fkjsdfsdf87890876543456789jl;ksjdl;fkj"
    );

    // Map 인 SECRET_KEY_SET 의 키값만 받아오는 keySet() 메소드를 이용해서 을 배열로 변환
    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static Random randomIndex = new Random();

    public static Pair<String, Key> getRandomKey() {
        // 0 ~ KID_SET 의 길이 범위의 난수 인덱스
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        // Map.get() : key 에 매칭되는 value 값을 넘겨줌
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if (key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }


}
