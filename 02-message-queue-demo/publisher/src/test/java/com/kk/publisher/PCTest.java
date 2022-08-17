package com.kk.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
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
        //4.创建队列
        String queueName = "hello.world.queue";
        channel.queueDeclare(queueName, true, false, false, null);
        //5.发布消息
        String message = "Hello World Type RabbitMQ";
        channel.basicPublish("", queueName, null, message.getBytes());
        //6.关闭通道，关闭连接
        channel.close();
        connection.close();
    }
}