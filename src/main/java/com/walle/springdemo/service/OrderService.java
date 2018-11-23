package com.walle.springdemo.service;

import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.OrderInfo;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.dao.MiaoshaOrderDao;
import com.walle.springdemo.dao.OrderDao;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private MiaoshaOrderDao miaoshaOrderDao;
    @Autowired
    private OrderDao orderDao;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return miaoshaOrderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    /**
     * 同时生成秒杀订单
     * @param user
     * @param goodsVo
     * @return
     */
    @Transactional
    public OrderInfo createrOrder(User user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setDeliveryAddrId(1l);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getGoodsPrice());
        orderInfo.getOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());//用orderId不对
        miaoshaOrder.setUserId(user.getId());
        miaoshaOrderDao.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;

    }
}
