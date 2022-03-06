package com.security.loginrememberme.config;


import com.security.loginrememberme.domain.SpUser;
import com.security.loginrememberme.service.SpUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInit implements InitializingBean {

    @Autowired
    private SpUserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {

        // isPresent() : 값이 있으면 ture, 없으면 false
        if(!userService.findUser("user1").isPresent()) {
            SpUser user = userService.save(SpUser.builder()
                            .email("user1")
                            .password("1111")
                            .enabled(true)
                            .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }

        if(!userService.findUser("user2").isPresent()) {
            SpUser user = userService.save(SpUser.builder()
                    .email("user2")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_USER");
        }
        if(!userService.findUser("admin").isPresent()) {
            SpUser user = userService.save(SpUser.builder()
                    .email("admin")
                    .password("1111")
                    .enabled(true)
                    .build());
            userService.addAuthority(user.getUserId(), "ROLE_ADMIN");
        }
    }
}
