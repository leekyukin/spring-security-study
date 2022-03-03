# RememberMeAuthenticationFilter

1. 세션이 없기 때문에 SecurityContextPersistenceFilter가 동작하지 않기 때문에 RememberMeAuthenticationFilter에서 동작하게 된다.
2. rememberMeServices.autoLogin()을 요청한다.
3. 상위 클레스인 AbstractRememberMeServices에서 extractRememberMeCookie()를 요청한다.
4. extractRememberMeCookie()는 HttpServletRequest 에서 remember-me 쿠키를 추출한다.
5. 쿠키의 유효성 검증을 한다.
6. 쿠키를 디코드한다. -> 아이디, 만료시간, 서명값이 추출된다.
7. 여기서부터 기본은 TokenBasedRememberMeServices로 동작한다.
8. 만료시간을 체크한다. (만료시간을 초과하면 빠이~)
9. 아이디로 UserDetails를 가져온다.
10. makeTokenSignature()가 userDeatils로 서명을 한다.
11. expectedTokenSignature에 서명한 값을 넣는다.
12. expectedTokenSignature와 브라우저에서 올라온 cookieToken과 같은지 판별한다.
13. authenticated가 true인 RememberMeAuthenticationToken이 발행된다.
14. 사용자는 재로그인 없이 페이지에 들어갈 수 있다.


## 기본적으로 제공하는 TokenBasedRememberMeSerivce를 사용하면 토큰이 브라우저에 남게 되고 누군가 고의적으로 토큰을 탈취했을시 비밀번호를 바꾸지 않는 이상 막기 힘들다.

