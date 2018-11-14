package com.walle.springdemo.utils;


import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class MD5Util {
    /**
     * 服务器端和客户端进行两次MD5加密，防止明文传输
     * 客户端先进行一次MD5加密（明文—+salt） 然后传输服务端
     * 服务端接收到之后把MD5和salt 写入数据库
     */
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    //固定加密字符串
    private static final String salt = "1b2c3v4e";

    //第一次MD5加密 用户输入密码
    public static String inputPassToFromPass(String inputPass) {
        //前面不加字符串的话char相加会变成ascii相加，结果会出错
        String str = "" + salt.charAt(0) + salt.charAt(4) + inputPass + salt.charAt(3) + salt.charAt(2);
        System.out.println(str);
        return md5(str);
    }

    //第二次MD5加密
    public static String formPassToDbPass(String formPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(4) + formPass + salt.charAt(3) + salt.charAt(2);
        return md5(str);
    }

    //用户输入密码转化成数据库密码
    public static String inputPassToDbPass(String input, String saltDb) {
        String fromPass = inputPassToFromPass(input);
        String dbPass = formPassToDbPass(fromPass, saltDb);
        return dbPass;
    }

    //随机生成salt
    public static String createSalt() {
        return UUID.randomUUID().toString().replace("-","").substring(0,8);
    }

    public static void main(String[] args) {
        String xx = inputPassToFromPass("123");//11e81013bd037fe241dc7bc7be009760
//        String xx = inputPassToDbPass("123", "xxx2123");
        System.out.println(createSalt());
        System.out.println(xx);
    }
}
