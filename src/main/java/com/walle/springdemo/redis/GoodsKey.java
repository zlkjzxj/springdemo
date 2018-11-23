package com.walle.springdemo.redis;

public class GoodsKey extends BasePrefix {


    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //设置页面缓存时间60s，防止瞬间访问量过大，时间太长的话，数据及时性太差
    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGetGoodsDetail = new GoodsKey(60, "name");
}
