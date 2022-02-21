package com.sp.fc.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;

// 두개 이상의 필터 체인을 구성하고 싶을 때 또 다른 Config 파일 을 만들어야 한다.
// 그때는 파일의 순서가 중요하다. 이때 @Order() 를 사용한다.
@Order(1) // 여러 필터 체인을 구성할 때 어떤 필터 체인이 우선인지 우선순위를 부여한다.
@EnableWebSecurity(debug = true)
// 요청이 올 때 마다 그 요청은 어떤 필터 체인을 타고 있는지 콘솔에 찍힌다.
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // WebSecurityConfigurerAdapter : application.yml 파일에서는 로그인 객체를 한 명만 추가 할 수 있다.
    // 따라서 여러가지의 권한을 테스트 하기 위해서 이 클레스를 상속받아 설정해줄 수 있다.


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password(passwordEncoder().encode("2222"))
                        .roles("USER")
                ).withUser(User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("3333"))
                        .roles("ADMIN"))
        ;
    }

    // password encoding : 비밀번호를 서버 구성과 특정 파일에서 임의로 볼 수 없게 만듬

    // passwordEnCoder 를 빈으로 등록하기
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 기본적으로 spring security 는 모든 페이지의 권한을 막고 시작한다. (인증을 받은 상태에서 접근 가능하게 되어있다.)
    // 만약 홈페이지 같이 기본적인 페이지는 모든 사람한테 접근을 허용하고 싶을때 configure 메소드를 상속받는다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // configure method 는 필터를 관리하는 곳이다.

        // 특정 요청에만 해당하는 필터 체인 구성하기
        // http.antMatcher("/**"); : 모든 요청
        // http.antMatcher("/api/**"); : api 밑으로 오는 것만

        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll()
                // 추가 설정 : 특정 페이지는 -> 모두 허용해라 ~
                        .anyRequest().authenticated());
                // 기본 설정 : 어떤 요청이든 -> 인증 받은 상태에서 접근해라~
        http.formLogin();
        http.httpBasic();
    }
}
