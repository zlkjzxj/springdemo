package com.walle.springdemo.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.walle.springdemo.rabbitmq.MqConfig.*;

@Service
public class MqReceiver {

    private static Logger logger = LoggerFactory.getLogger(MqReceiver.class);

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
}
