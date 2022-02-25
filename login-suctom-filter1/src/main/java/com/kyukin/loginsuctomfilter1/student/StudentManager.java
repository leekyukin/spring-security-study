package com.kyukin.loginsuctomfilter1.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {
    // 인증 제공자(AuthenticationProvider) 가 될 객체

    // inMemoryDB     id    student
    private HashMap<String, Student> studentDB = new HashMap<>();


    // token(통행증) 발행하기
    // AuthenticationProvider 는 authenticate() 을 통해서 Authentication 을
    // 받아서 인증하고 Authentication 에 담아서 다시 객체로 반환한다.
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        // authenticate() 의 관심 대상을 Custom 한 Token 으로 형변환
        StudentAuthenticationToken token =
                (StudentAuthenticationToken) authentication;
        // DB의 아이디가 토큰에 name 과 같다면
        if(studentDB.containsKey(token.getCredentials())) {
            Student student = studentDB.get(token.getCredentials());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;
        // 인증 실패시 null 반환
    }

    // AuthenticationProvider 가 관심을 갖고 있는 대상은
    // StudentAuthenticationToken 와 같은지 확인하는 메서드
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student(
                        "hong",
                        "홍길동",
                        Set.of(
                                new SimpleGrantedAuthority("ROLE_STUDENT")
                        )
                ),
                new Student(
                        "Kang",
                        "강아지",
                        Set.of(
                                new SimpleGrantedAuthority("ROLE_STUDENT")
                        )
                ),
                new Student(
                        "rang",
                        "호랑이",
                        Set.of(
                                new SimpleGrantedAuthority("ROLE_STUDENT")
                        )
                )
        ).forEach(s->
                studentDB.put(s.getId(), s)
        );
    }
}
