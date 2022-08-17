package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyFanoutConsumer {
    @RabbitListener(queues = {"fanout.queue1"})
    public void testFanout1(String message) {
        System.out.println("广播模式...消费者...1...消费消息：" + message);
    }

    @RabbitListener(queues = {"fanout.queue2"})
    public void testFanout2(String message) {
        System.out.println("广播模式...消费者...2...消费消息：" + message);
    }

    @RabbitListener(queues = {"fanout.queue3"})
    public void testFanout3(String message) {
        System.out.println("广播模式...消费者...3...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "fanout.queue4"), exchange = @Exchange(value = "fanout.exchange", type = "fanout"))})
    public void testFanout4(String message) {
        System.out.println("广播模式...消费者...4...消费消息：" + message);
    }
}
