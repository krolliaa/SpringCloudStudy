package com.kk.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration2 {
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    @Bean
    public Queue directQueue1() {
        return new Queue("direct.queue1");
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("direct.queue2");
    }

    @Bean
    public Queue directQueue3() {
        return new Queue("direct.queue3");
    }

    @Bean
    public Binding bindingDirectQueue1() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("cold.coffee");
    }

    @Bean
    public Binding bindingDirectQueue2() {
        return BindingBuilder.bind(directQueue2()).to(directExchange()).with("hot.coffee");
    }

    @Bean
    public Binding bindingDirectQueue3() {
        return BindingBuilder.bind(directQueue3()).to(directExchange()).with("spicy.coffee");
    }
}
