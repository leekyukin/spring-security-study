package com.example.jwttest.global.security;

import com.example.jwttest.global.jwt.JwtAuthenticationFilter;
import com.example.jwttest.global.jwt.JwtAuthorizationFilter;
import com.example.jwttest.user.domain.repository.UserRepository;
import com.example.jwttest.user.domain.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/signup", "/login").permitAll()
                .antMatchers("/profile").hasRole("USER")
                .anyRequest().authenticated()
                ;

        http
                // http.addFilterBefore : 두번쨰 파라미터가 되는 필터 앞에 커스텀 필터를 배치하는 method
                .addFilterBefore(
                        new JwtAuthenticationFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class
                ).addFilterBefore(
                        new JwtAuthorizationFilter(userRepository),
                        BasicAuthenticationFilter.class
                );

    }
}
