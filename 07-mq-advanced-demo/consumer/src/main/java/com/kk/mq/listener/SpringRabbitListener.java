package com.kk.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringRabbitListener {
    @RabbitListener(queues = "consumer.queue")
    public void listenSimpleQueue(String msg) {
        System.out.println(1 / 0);
        System.out.println("消费者接收到 consumer.queue 的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "delay.queue", durable = "true"), exchange = @Exchange(name = "delay.direct", durable = "true", delayed = "true"), key = "delay"))
    public void listenDelayedQueue(String msg) {
        log.info("接收到 delay.queue 的延迟消息：{}", msg);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "lazy.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")))
    public void listenLazyQueue(String msg) {
        log.info("接收到 lazy.queue 的延迟消息：{}", msg);
    }
}
