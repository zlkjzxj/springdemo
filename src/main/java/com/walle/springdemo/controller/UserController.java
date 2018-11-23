package com.walle.springdemo.controller;

import com.walle.springdemo.bean.User;
import com.walle.springdemo.result.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/testInfo")
    public Result<User> testInfo(Model model) {
        User user = new User();
        user.setName("李承乾");
        return Result.success(user);
    }

    @RequestMapping("/userInfo")
    public Result<User> getUserInfo(Model model, User user) {
        model.addAttribute("user", user);
        System.out.println(user.getName());
        return Result.success(user);
    }
}
