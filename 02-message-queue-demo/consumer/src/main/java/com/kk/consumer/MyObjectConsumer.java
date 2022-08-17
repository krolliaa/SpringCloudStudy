package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyObjectConsumer {

    @RabbitListener(queues = {"object.queue"})
    public void testTopic1(Map<String, String> map) {
        System.out.println("接收 Map 消息：" + map);
    }
}
