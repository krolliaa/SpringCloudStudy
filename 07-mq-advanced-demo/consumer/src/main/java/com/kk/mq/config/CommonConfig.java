package com.kk.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("consumer.exchange");
    }

    @Bean
    public Queue directQueue() {
        return new Queue("consumer.queue");
    }

    @Bean
    public Binding bindingDirectExchangeAndDirectQueue() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with("consumer.confirm");
    }

    @Bean
    public DirectExchange errorMessageExchange() {
        return new DirectExchange("error.exchange");
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue", true);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorMessageExchange) {
        return BindingBuilder.bind(errorQueue).to(errorMessageExchange).with("error");
    }

    @Bean
    public MessageRecoverer messageRecoverer() {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.exchange", "error");
    }

    @Bean
    public DirectExchange delayedExchange() {
        return ExchangeBuilder.directExchange("delay.direct").delayed().durable(true).build();
    }

    @Bean
    public Queue delayedQueue() {
        return new Queue("delay.queue");
    }

    @Bean
    public Binding delayedBinding() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with("delay");
    }


    @Bean
    public Queue lazyQueue() {
        return QueueBuilder.durable("lazy.queue").lazy().build();
    }
}