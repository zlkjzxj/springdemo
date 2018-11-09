package com.walle.springdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class LoginController {

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
}
