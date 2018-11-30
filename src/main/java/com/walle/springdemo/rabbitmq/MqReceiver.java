package com.walle.springdemo.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.OrderInfo;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.service.GoodsService;
import com.walle.springdemo.service.MiaoshaService;
import com.walle.springdemo.service.OrderService;
import com.walle.springdemo.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.walle.springdemo.rabbitmq.MqConfig.*;

@Service
public class MqReceiver {

    private static Logger logger = LoggerFactory.getLogger(MqReceiver.class);

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaService miaoshaService;

    /**
     * direct 模式 exchange 交换机
     * <p>
     * 数据先发动到交换机上，做了一层路由，然后再发送出去
     *
     * @param message
     */
    @RabbitListener(queues = QUEUE)
    public void receive(String message) {
        logger.info("receive message:" + message);
    }

    @RabbitListener(queues = TOPIC_QUEUE1)
    public void receiveTopicQueue1(String message) {
        logger.info("receive topic queue1 message:" + message);
    }

    @RabbitListener(queues = TOPIC_QUEUE2)
    public void receiveTopicQueue2(String message) {
        logger.info("receive topic queue2 message:" + message);
    }

    @RabbitListener(queues = HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message) {
        logger.info("receive header queue message:" + new String(message));
    }


    @RabbitListener(queues = MIAOSHA_QUEUE)
    public void miaoshaReceive(String message) {
        logger.info("receive message:" + message);
        MiaoshaMsg msg = (MiaoshaMsg) JSONObject.toJSON(message);
        User user = msg.getUser();
        long goodsId = msg.getGoodsId();

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVo(goodsId);
        if (goodsVo.getStockCount() <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存，下订单，一并完成
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
        goodsService.reduceStock(goodsId);
    }
}
