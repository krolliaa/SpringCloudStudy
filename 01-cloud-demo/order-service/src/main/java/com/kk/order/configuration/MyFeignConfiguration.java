package com.kk.order.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class MyFeignConfiguration {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
