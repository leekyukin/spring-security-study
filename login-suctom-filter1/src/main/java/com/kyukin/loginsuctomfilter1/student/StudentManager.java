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
    // provider 가 될 객체

    // inMemoryDB     id    student
    private HashMap<String, Student> studentDB = new HashMap<>();


    // token(통행증) 발행하기
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

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
    }

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
