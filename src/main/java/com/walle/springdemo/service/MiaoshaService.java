package com.walle.springdemo.service;

import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.OrderInfo;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.redis.MiaoshaKey;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MiaoshaService {


    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goodsVo) {
        //减库存 Miaosha goods 的stock_count-1
        int i = goodsService.reduceStock(goodsVo.getId());
        if (i > 0) {//加判断是有可能减库存失败了，就不要去生成订单了，而不是方法执行错误
            //生成订单 insert miaosha_order
            OrderInfo orderInfo = orderService.createrOrder(user, goodsVo);
            return orderInfo;
        } else {//库存数量不足的时候设置是否卖光标记为true
            setGoodsOver(goodsVo.getId());
        }
        return null;
    }


    public long getResult(long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
        if (order != null) {//秒杀成功且已经写入订单
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;//已经卖完了
            } else {
                return 0;//轮询
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.isExists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }
}
