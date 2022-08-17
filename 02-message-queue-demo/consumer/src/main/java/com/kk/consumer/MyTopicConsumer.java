package com.kk.consumer;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyTopicConsumer {

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue1"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"china.#"})})
    public void testTopic1(String message) {
        System.out.println("消费者...1...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue2"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"japan.#"})})
    public void testTopic2(String message) {
        System.out.println("消费者...2...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue3"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"#.weather"})})
    public void testTopic3(String message) {
        System.out.println("消费者...3...消费消息：" + message);
    }

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "topic.queue4"), exchange = @Exchange(value = "topic.exchange", type = "topic"), key = {"#.news"})})
    public void testTopic4(String message) {
        System.out.println("消费者...4...消费消息：" + message);
    }
}
