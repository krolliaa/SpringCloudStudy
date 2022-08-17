package com.kk.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAMQPTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimple() {
        String queueName = "hello.world.queue";
        String message = "人类毁于傲慢。";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWork() throws InterruptedException {
        String queueName = "work.queue";
        String message = "世界人民 --- ";
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, (message + i));
            Thread.sleep(20);
        }
    }

    @Test
    public void testFanout() {
        String message = "Fanout Message";
        rabbitTemplate.convertAndSend("fanout.exchange", "", message.getBytes());
    }

}
