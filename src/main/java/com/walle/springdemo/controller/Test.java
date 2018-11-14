package com.walle.springdemo.controller;

import com.walle.springdemo.bean.User;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.redis.UserKey;
import com.walle.springdemo.result.Result;
import com.walle.springdemo.service.UserService;
import com.walle.springdemo.utils.MD5Util;
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
    public User getUser() {
        User user = new User();
        user.setName("zhangsan");
        user.setPassword("fuck");
        user.setAge(20);
        user.setBirth(new Date());
        return user;
    }

    @RequestMapping("/thymeleaf")
    public String hello(Model model) {
        model.addAttribute("name", "thymeleaf");
        return "index";
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
        User user = new User();
        user.setId(10010);
        user.setName("admin");
        String salt = MD5Util.createSalt();
        user.setPassword(MD5Util.inputPassToDbPass("123", salt));
        user.setSalt(salt);
        int code = userService.insertUser(user);
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
