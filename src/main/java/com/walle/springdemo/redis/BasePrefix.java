package com.walle.springdemo.redis;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public BasePrefix(String prefix) {//抽象类别人没法new
        this(-1, prefix);
    }

    @Override
    public int expireSeconds() {//默认-1代表永不过期
        return -1;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
