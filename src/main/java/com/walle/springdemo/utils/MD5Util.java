package com.walle.springdemo.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    //固定加密字符串
    private static final String salt = "1b2c3v4e";

    //第一次MD5加密
    public static String inputPassToFromPass(String inputPass) {
        String str = salt.charAt(0) + salt.charAt(4) + inputPass + salt.charAt(3) + salt.charAt(2);
        return md5(str);
    }

    //第二次MD5加密
    public static String fromPassToDbPass(String formPass, String salt) {
        String str = salt.charAt(0) + salt.charAt(4) + formPass + salt.charAt(3) + salt.charAt(2);
        return md5(str);
    }

    //密码写入数据库
    public static String inputPassToDbPass(String input, String saltDb) {
        String fromPass = inputPassToFromPass(input);
        String dbPass = fromPassToDbPass(fromPass, saltDb);
        return dbPass;
    }


    public static void main(String[] args) {
        String xx = inputPassToDbPass("123", "xxx2123");
        System.out.println(xx);
    }
}
