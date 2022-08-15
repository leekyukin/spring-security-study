package com.study.securitypractice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/role/user")
    public String user() {
        return "USER";
    }

    @GetMapping("/role/admin")
    public String admin() {
        return "ADMIN";
    }
}
