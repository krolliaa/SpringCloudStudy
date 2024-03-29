package com.kk.configuration;

import com.kk.clients.fallbackfactory.UserClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;

public class FeignConfiguration {
    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public UserClientFallbackFactory userClientFallbackFactory() {
        return new UserClientFallbackFactory();
    }
}
