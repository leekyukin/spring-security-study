package com.study.inflearnsecurity;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                ;
        http
                .formLogin()
                // .loginPage("/loginPage")  // security 에서 제공하는 로그인 페이지 이외 사용자 정의 로그인 페이지
                .defaultSuccessUrl("/") // 로그인 성공 후 이동할 url
                .failureUrl("/login") // 로그인 실패 후 이동할 url
                .usernameParameter("userid")    // 아이디 파라미터명 설정
                .passwordParameter("passwd")    // 비밀번호 필드명 설정
                .loginProcessingUrl("/login_proc")   // from 태그의 action url 변경 | default : login
                .successHandler(new AuthenticationSuccessHandler() {    // 로그인 성공시 함수 호출
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication : " + authentication.getName());    // 인증된 객체의 이름 보여주기
                        response.sendRedirect("/");     // redirect : url root
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("exception : " + exception.getMessage());   // 에러 메세지 출
                        response.sendRedirect("/login");
                    }
                })
                .permitAll()    // loginPage 의 접근을 인증없이 허용함
        ;

        http
                .logout()
                .logoutUrl("/logout")   // default : /logout
                .logoutSuccessUrl("/login") // login 성공 후 이동 페이지
                .addLogoutHandler(new LogoutHandler() { // 로그아웃 핸들러 설정
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        HttpSession session = request.getSession();
                        session.invalidate();   // 세션 무효화
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {  // 로그아웃 성공 후 핸들러 호출
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/login");
                    }
                })
                .deleteCookies("remember-me")
                ;
    }
}
