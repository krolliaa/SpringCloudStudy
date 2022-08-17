package com.kk.consumer;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootTest()
public class PCTest {
    @Test
    public void HelloWorldTest() throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.56.1");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123456");
        //2.创建连接
        Connection connection = connectionFactory.newConnection();
        //3.创建连接通道
        Channel channel = connection.createChannel();
        //4.消费者订阅消息
        String queueName = "hello.world.queue";
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("接收到消息：" + message);
            }
        });
        System.out.println("等待接收消息");
        //5.关闭连接通道
        channel.close();
        connection.close();
    }
}
