package com.kk.mq.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() throws InterruptedException {
        String routingKey = "simple";
        String message = "hello, spring amqp!";
        rabbitTemplate.convertAndSend("amqp.topic", routingKey, message);
    }

    @Test
    public void testPublisherConfirm() throws InterruptedException {
        String message = "Hello, Consumer Queue!";
        //这里的 UUID.randomUUID().toString() 的意思是定义唯一全局 ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //添加回调函数
        correlationData.getFuture().addCallback(result -> {
            if (result.isAck()) {
                log.info("消息从发送者到交换机的过程成功，ID:{}", correlationData.getId());
            } else {
                log.info("发送者到交换机的过程消息丢失，消息发送到路由器失败, ID:{}, 原因{}", correlationData.getId(), result.getReason());
            }
        }, throwable -> {
            log.error("消息从发送者到交换机的过程中发生异常, ID:{}, 原因{}", correlationData.getId(), throwable.getMessage());
        });
        Message msg = MessageBuilder.withBody(message.getBytes(StandardCharsets.UTF_8)).setDeliveryMode(MessageDeliveryMode.PERSISTENT).setHeader("x-delay", 10000).build();
        rabbitTemplate.convertAndSend("consumer.exchange", "consumer.confirm", msg, correlationData);
        //等待 ACK 回执
        Thread.sleep(2000);
    }
}
