package com.kyukin.loginsuctomfilter1.teacher;

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
public class TeacherManager implements AuthenticationProvider, InitializingBean {
    // provider 가 될 객체

    // inMemoryDB     id    Teacher
    private HashMap<String, Teacher> teacherDB = new HashMap<>();


    // token 발행하기
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) authentication;
        // DB의 아이디가 토큰에 name 과 같다면
        if(teacherDB.containsKey(token.getName())) {
            Teacher teacher = teacherDB.get(token.getName());
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher(
                        "choi",
                        "최선",
                        Set.of(
                                new SimpleGrantedAuthority("ROLE_TEACHER")
                        )
                )
        ).forEach(t->
                teacherDB.put(t.getId(), t)
        );
    }
}
