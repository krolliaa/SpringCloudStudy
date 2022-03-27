package com.zwm.order.util;

import com.alibaba.fastjson.JSONObject;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class BeanUtils {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JSONObject getJsonObject() {
        return new JSONObject();
    }

    @Bean
    public RandomRule randomRule() {
        return new RandomRule();
    }
}
