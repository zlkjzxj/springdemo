package com.walle.springdemo.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.walle.springdemo.rabbitmq.MqConfig.*;

@Service
public class MqSender {

    private static Logger logger = LoggerFactory.getLogger(MqSender.class);
    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message) {
        String msg = JSONObject.toJSONString(message);
        logger.info("send message:" + msg);
        amqpTemplate.convertAndSend(QUEUE, msg);
    }

    public void sendTopic(Object message) {
        String msg = JSONObject.toJSONString(message);
        logger.info("send topic message:" + msg);
        amqpTemplate.convertAndSend(TOPIC_EXCHANGE, "topic.key1", msg + "1");
        amqpTemplate.convertAndSend(TOPIC_EXCHANGE, "topic.key2", msg + "2");
    }

    public void sendFanout(Object message) {
        String msg = JSONObject.toJSONString(message);
        logger.info("send fanout message:" + msg);
        amqpTemplate.convertAndSend(FANOUT_EXCHANGE, "", msg + "1");
    }

    public void sendHeader(Object message) {
        String msg = JSONObject.toJSONString(message);
        logger.info("send header message:" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1", "value1");
        properties.setHeader("header2", "value2");
        Message message1 = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(HEADERS_EXCHANGE, "", message1);
    }
}
