package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyConsumer {
    @RabbitListener(queues = {"hello.world.queue"})
    public void testSimple(String message) {
        System.out.println("消费者消费消息：" + message);
    }

    @RabbitListener(queues = {"work.queue"})
    public void testWork1(String message) {
        System.out.println("消费者...1...消费消息：" + message);
    }

    @RabbitListener(queues = {"work.queue"})
    public void testWork2(String message) {
        System.out.println("消费者...2...消费消息：" + message);
    }
}
