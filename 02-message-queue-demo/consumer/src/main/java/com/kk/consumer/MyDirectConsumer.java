package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyDirectConsumer {

    @RabbitListener(queues = {"direct.queue1"})
    public void testDirect1(String message) {
        System.out.println("消费者 ① 消费消息 冷咖啡：" + message);
    }

    @RabbitListener(queues = {"direct.queue2"})
    public void testDirect2(String message) {
        System.out.println("消费者 ② 消费消息 热咖啡：" + message);
    }

    @RabbitListener(queues = {"direct.queue3"})
    public void testDirect3(String message) {
        System.out.println("消费者 ③ 消费消息 辣咖啡：" + message);
    }


    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "direct.queue4"), exchange = @Exchange(value = "direct.exchange", type = "direct"), key = {"sweet.coffee", "hot.coffee"})})
    public void testDirect4(String message) {
        System.out.println("消费者 ④ 消费消息 热咖啡和甜咖啡：" + message);
    }
}
