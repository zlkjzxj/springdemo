package com.walle.springdemo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.Date;

public class User1 {
    private String name;
    @JsonIgnore  // ignore this ,json don't out it
    private String password;

    private int age;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", locale = "zh", timezone = "")
    private Date birth;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String desc;

    public User1() {
    }

    public User1(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
