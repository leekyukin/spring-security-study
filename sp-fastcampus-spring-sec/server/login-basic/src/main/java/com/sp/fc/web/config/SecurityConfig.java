package com.sp.fc.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
// (prePostEnabled = true) :
// Controller 의 @PreAuthorize("hasAnyAuthority('권한')") 의 권한 대로 작동하게 됨
// 만약 허용되지 않은 페이지로 접근시 403 에러가 발생한다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final CustomAuthDetails customAuthDetails;

    public SecurityConfig(CustomAuthDetails customAuthDetails) {
        this.customAuthDetails = customAuthDetails;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                ).withUser(
                        User.withDefaultPasswordEncoder()
                        // 테스트 시에만 한정하여 사용할 수 있는 비밀번호 인코더
                                .username("admin")
                                .password("2222")
                                .roles("ADMIN")
                );
        // 기본적으로는 csrf 토큰이 없기 때문에 로그인이 되지 않는다.
        // 우리는 로그인의 form 태그에 action="/login" 을 thymeleaf 형식으로
        // h:action="@{/login}" 수정 함으로써 문제를 해결했다.
        // thymeleaf 형식으로만 바꿔주면 자동으로 csrf 토큰이 생긴다.
        // chrome 개발자 모드로 form 태그를 살펴보면 다음줄에
        // 토큰을 담은 input 태그가 hidden 으로 생긴다.
    }

    CsrfFilter csrfFilter;
    UsernamePasswordAuthenticationFilter filter;

    // 관리자는 관리자 페이지 뿐만 아니라 유저가 기본적으로 사용하는 페이지에도 제약이 없어야 한다.
    // hierarchy : 계층
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests -> {
                    requests
                            // 전부 허용
                            .antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                            ;
                })
                .formLogin(
                        // : 로그인 페이지를 지정해주지 않으면
                        // DefaultLoginPageGeneratingFilter 와 DefaultLogoutGeneratingFilter 가
                        // 동시에 동작하게 된다.
                        login->login.loginPage("/login")
                                .permitAll()
                                // 로그인 페이지 자체는 권한을 받아야 들어올 수 있는 페이지 인데
                                // 로그인 페이지 로 가려면 로그인 받고 오라고 하기 때문에
                                // 무한 루프에 빠질 수 있다.
                                // 그래서 로그인 페이지는 꼭 permitAll() 을 해주어야한다.
                                .defaultSuccessUrl("/", false)
                                // alwaysUse : 로그인 하기 전에 다른 페이지에 들어갔다가 권한이 필요해서 로그인 페이지로 튕겨서 로그인을 했을 떄
                                // alwaysUse 가 true 면 항상 지정된 페이지(여기서는 "/") 로 가고, false 면 유동적으로 로그인 페이지 이전에 가장 최근으로 들어갔던
                                // 페이지로 들어가게 된다.
                                .failureUrl("/login-error") // 로그인 실패시 디자인된 로그인 에러 페이지로 이동
                                .authenticationDetailsSource(customAuthDetails)
                )
                .logout(logout->logout.logoutSuccessUrl("/"))
                // 로그아웃을 했을 때 로그아웃 성공시 지정 페이지(여기서는 "/" 로 이동
                .exceptionHandling(exception->
                        exception.accessDeniedPage("/access-denied"))
                // 403 에러 (권한 부족)이 발생시 지정 페이지(여기서는 "/access-denied") 로 이동
                ;
    }


    // 기본적으로는 보안 떄문에 리소스에 css 가 적용되지 않는다.
    // web resource 에 한해서는 보안이 Security Filter 가 적용되지 않도록 ignoring 시켜줘야한다.
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                        // css, js, images, webjars, favicon 등이 포함되어 있다.
                )
                ;
    }
}
