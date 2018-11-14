package com.walle.springdemo.result;

public class CodeMsg {
    private int code;
    private String msg;

    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg USERNOTEXIST_ERROR = new CodeMsg(500200, "用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500300, "密码错误");

    // 登录模块

    // 商品模块

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
