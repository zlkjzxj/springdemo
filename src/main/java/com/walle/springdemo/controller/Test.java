package com.walle.springdemo.controller;

import com.walle.springdemo.bean.User;
import com.walle.springdemo.bean.User1;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.redis.UserKey;
import com.walle.springdemo.result.Result;
import com.walle.springdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class Test {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/getUser")
    @ResponseBody
    public User1 getUser() {
        User1 user = new User1();
        user.setName("zhangsan");
        user.setPassword("fuck");
        user.setAge(20);
        user.setBirth(new Date());
        return user;
    }

    @RequestMapping("/thymeleaf")
    public String hello(Model model) {
        model.addAttribute("name", "thymeleaf");
        return "hello";
    }

    @RequestMapping("/db")
    @ResponseBody
    public Result<User> dbGet(Model model) {
        User user = userService.getUserById(1000);
        return Result.success(user);
    }

    @RequestMapping("/insert")
    @ResponseBody
    public Result<Integer> xx(Model model) {

        int code = userService.insert(null);
        return Result.success(code);
    }

    @RequestMapping("/getUsers")
    @ResponseBody
    public Result<List<User>> getUsers() {
        return Result.success(userService.getUsers());
    }

    @RequestMapping("/getRedis")
    @ResponseBody
    public Result<User> getRedis() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/setRedis")
    @ResponseBody
    public Result<Boolean> setRedis() {
        User user = new User();
        user.setId(1);
        user.setName("shit");
        boolean b = redisService.set(UserKey.getById, "" + 1, user);
        return Result.success(true);
    }
}
