package com.study.jwtstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class JwtStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtStudyApplication.class, args);
    }

}
