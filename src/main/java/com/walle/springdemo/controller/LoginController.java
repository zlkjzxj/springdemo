package com.walle.springdemo.controller;

import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.result.Result;
import com.walle.springdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/")
    public String toLogin() {
        return "login";
    }

    /**
     * 用户登录，传过来用户名和密码，密码是经过MD5加密的
     *
     * @param response
     * @param name
     * @param password
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Result<Boolean> login(HttpServletResponse response, @RequestParam("name") String name, @RequestParam("password") String password) {
        //根据用户名去数据库查询用户
        userService.login(response, name, password);
        return Result.success(true);
    }
}
