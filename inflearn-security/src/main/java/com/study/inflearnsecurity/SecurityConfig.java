package com.study.inflearnsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                                                                // 비밀번호 암호화 정책을 prefix 로 적어줘야함
                                                                    //    [noop] -> none
        auth.inMemoryAuthentication().withUser("user").password("{noop}1234").roles("USER");
        auth.inMemoryAuthentication().withUser("sys").password("{noop}1234").roles("SYS", "USER");
        auth.inMemoryAuthentication() .withUser("admin").password("{noop}1234").roles("ADMIN","SYS","USER");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
                .anyRequest().authenticated()
        ;
        http
                .formLogin()
        ;
    }
}
