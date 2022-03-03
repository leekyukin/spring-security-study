# PersistenceTokenBasedRememberMeServices

기존의 TokenBasedRememberMeServices의 보안적인 문제를 해결하기 위해서 브라우저가 아닌 서버에 토큰이 저장되는 방식이다.

- **포멧** : series:token
- 토큰에 username이 노출되지 않고, 만료시간도 노출되지 않는다. 만료시간은 서버에서 정하고 노출하지 않고 서버는 로그인 시간만 저장한다.
- series 값이 키가 된다. 일종의 채널이라고 보면 편하다.
- 대신 재로그인이 될 때마다 token 값을 갱신해 준다. 그래서 토큰이 탈취되어 다른 사용자가 다른 장소에서 로그인을 했다면 정상 사용자가 다시 로그인 할 때, CookieTheftException이 발생하게 되고, 서버는 해당 사용자로 발급된 모든 remember-me 쿠키값들을 삭제하고 재로그인을 요청하게 된다.
- InememoryTokenRepository는 서버가 재시작하면 등록된 토큰들이 사라진다. 따라서 자동로그인을 설정했더라도 다시 로그인을 해야 한다. 재시작 후에도 토큰을 남기고 싶다면 JdbcTokenRepository를 사용하거나 이와 유사한 방법으로 토큰을 관리해야 한다.
- 로그아웃하게 다른 곳에 묻혀놓은 remember-me 쿠키값도 쓸모가 없게 된다. 만약 다른 곳에서 remember-me로 로그인한 쿠키를 살려놓고 싶다면, series로 삭제하도록 logout을 수정해야 한다.
 
# remember-me token

PersistenceTOkenBasedRememberMeServices에서 발행된 remember-me 토큰은 디코드하면
기존과는 다른 방식으로 발행됐다는것을 알 수 있다.

### **remember-me token** : OGVVQXUw...
### **decode** : 8eUAu0J23vY14bCVcpYTUw%3D%3D:su64weY%2BYrZgFByO8DupzA%3D%3D


|SERIES|TOKEN|
|-|-|
|8eUAu0J23vY14bCVcpYTUw%3D%3D|su64weY%2BYrZgFByO8DupzA%3D%3D|

<hr>

<img src="../img/BASE64.png" >

토큰 디코드는 위사이트에서 가능하다.


## 코드 

### config 설정

```java
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SpUserService userService;
    private final DataSource dataSource;

    public SecurityConfig(SpUserService userService, DataSource dataSource) {
        this.userService = userService;
        this.dataSource = dataSource;
    }

    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        try {
            repository.removeUserTokens("1"); // 절대 DB에 없을 값을 넣어서 catch 가 실행되게 함
        }catch(Exception e) {
            repository.setCreateTableOnStartup(true);
        }
        return repository;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices service =
                new PersistentTokenBasedRememberMeServices("hello",
                        userService,
                        tokenRepository()
                );
        return service;
    }

}
```