package com.study.inflearnsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .anyRequest().authenticated()
        ;
        http
                .formLogin()
        ;
        http
                .sessionManagement()
                .sessionFixation().none() // 기본값 // 접속할때마다 새로운 세션 발급, 서블릿 3.1 이상에서 기본값
                                                     // none : 새로 생성하지 않음, 공격당함
                                                     // migrateSession : 서블릿 3.1 이하에서 기본값, changeSessionId()와 같음
                                                     // newSession : JSESSIONID 가 새로 발급되지만 이전 세션에서 설정한 속성값은 초기화 된다.
        ;

    }
}
