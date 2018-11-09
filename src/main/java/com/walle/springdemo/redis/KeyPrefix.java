package com.walle.springdemo.redis;

public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
