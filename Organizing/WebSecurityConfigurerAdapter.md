# WebSecurityConfigurerAdapter

### 로그인과 관련된 여러가지 권한에 대한 필터를 구성할 수 있다.

```java
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    ...
}
```
기본적으로 위와 같이 상속받고 시작한다.
- <span style="color:yellow">@EnableWebSecurity(debug = true)</span> : spring security 와 관련된 정보를 콘솔창에서 확인할 수 있다.
- <span style="color:yellow">@EnableGlobalMethodSecurity(prePostEnabled = true)</span> : Controller에서 설정한 어노테이션 @PreAuthorize("hasAnyAuthority('권한')") 의 권한 대로 작동 하도록 한다. 만약 허용되지 않은 페이지로 접근시 403 에러가 발생한다.


# Overriding

## 테스트에 사용할 User 객체 생성하기

```java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                // 일반 유저 권한의 객체 생성
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                // 관리자 권한의 객체 생성
                ).withUser(
                        User.withDefaultPasswordEncoder()
                        // 테스트 시에만 한정하여 사용할 수 있는 비밀번호 인코더
                                .username("admin")
                                .password("2222")
                                .roles("ADMIN")
                );
    }
```
기본적으로는 csrf 토큰이 없기 때문에 로그인이 되지 않는다.
우리는 로그인의 form 태그에 action="/login" 을 thymeleaf 형식으로
h:action="@{/login}" 수정 함으로써 문제를 해결했다.
thymeleaf 형식으로만 바꿔주면 자동으로 csrf 토큰이 생긴다.
chrome 개발자 모드로 form 태그를 살펴보면 다음줄에
토큰을 담은 input 태그가 hidden 으로 생긴다.

<hr>
<br>

## 관리자에게 일반 사용자 권리까지 주기

### 관리자는 관리 페이지 외에 다른 페이지 이동에도 제약이 없어야 한다. 관리자 권한뿐만 아니라 유저 권한도 있어야 한다.
### hierarchy : 계층
```java
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }
```

<hr>
<br>

## resource 적용시키기

### 기본적으로는 보안 때문에 리소스에 css나 js 파일이 적용되지 않는다.

```java
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                        // css, js, images, webjars, favicon 등이 포함되어 있다.
                )
                ;
    }   
```
### web resource에 한해서는 Security Filter가 적용되지 않도록 ignoring 시켜줘야 한다.

<hr>
<br>

## 여러가지 ~~~

```java
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
                                .failureUrl("/login-error") 
                                .authenticationDetailsSource(customAuthDetails)
                )
                .logout(logout->logout.logoutSuccessUrl("/"))
                // 로그아웃을 했을 때 로그아웃 성공시 지정 페이지(여기서는 "/" 로 이동
                .exceptionHandling(exception->
                        exception.accessDeniedPage("/access-denied"))
                // 403 에러 (권한 부족)이 발생시 지정 페이지(여기서는 "/access-denied") 로 이동
                ;
    }
```
- http 
    -  **authorizeRequests()** : 특정 경로에 특정한 권한을 가진 요청에 대한 구성
        -  **antMatchers()** : 특정 경로를 지정 / **anyRequest** : 모든 요청을 지정
            - **permitAll()** : 모든 권한 허용
            - **authenticated** : 그 페이지는 로그인을 요구함
            - **hasRole()** : 특정 권한만 허용 
        
    - **formLogin()** : 로그인 페이지를 지정해주지 않으면 DefaultLoginPageGeneratingFilter와 DefaultLogoutGeneratingFilter가 동시에 동작하게 된다.
        - **defaultSuccessUrl()** : 위 주석 대로 ~
        - **failureUrl()** : 로그인 실패시 디자인된 로그인 에러 페이지로 이동



