package com.walle.springdemo.result;

public class CodeMsg {
    private int code;
    private String msg;

    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg USERNOTEXIST_ERROR = new CodeMsg(500101, "用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500102, "密码错误");
    public static CodeMsg BIND_ERROR = new CodeMsg(500103, "参数校验异常：%s");

    // 登录模块

    // 商品模块

    //秒杀模块
    public static CodeMsg MISAOSHA_OVER = new CodeMsg(500400, "库存不足");
    public static CodeMsg MISAOSHA_REPEAT = new CodeMsg(500401, "已经秒杀过");

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String msg = String.format(this.msg, args);
        return new CodeMsg(code, msg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
