package com.walle.springdemo.controller;

import com.walle.springdemo.bean.User;
import com.walle.springdemo.result.CodeMsg;
import com.walle.springdemo.result.Result;
import com.walle.springdemo.service.UserService;
import com.walle.springdemo.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    @ResponseBody
    public Result<Object> login(@RequestParam("name") String name, @RequestParam("password") String password, Model model) {
        //根据用户名去数据库查询用户
        User user = userService.getUserByName(name);
        //如果查到用户 就用加密的密码和数据库的salt再次加密比对密码是否一样，如果一样，登录成功
        if (user != null) {
            String pass = MD5Util.formPassToDbPass(password, user.getSalt());
            if (pass.equals(user.getPassword())) {
                return Result.success(user);
            } else {
                return Result.error(CodeMsg.PASSWORD_ERROR);
            }
        }
        return Result.error(CodeMsg.USERNOTEXIST_ERROR);
    }
}
