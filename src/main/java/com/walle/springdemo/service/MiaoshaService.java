package com.walle.springdemo.service;

import com.walle.springdemo.bean.Goods;
import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.OrderInfo;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.dao.GoodsDao;
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

    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goodsVo) {
        //减库存 Miaosha goods 的stock_count-1
        int i = goodsService.reduceStock(goodsVo.getId());
        //生成订单 insert miaosha_order
        OrderInfo orderInfo = orderService.createrOrder(user, goodsVo);
        return orderInfo;
    }
}
