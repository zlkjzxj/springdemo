package com.walle.springdemo.redis;

public class UserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600 * 24;

    public UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey getById = new UserKey(0, "id");
    public static UserKey getByName = new UserKey(0, "name");
    public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");
}
