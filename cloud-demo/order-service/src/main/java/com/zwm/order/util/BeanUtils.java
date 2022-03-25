package com.zwm.order.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class BeanUtils {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JSONObject getJsonObject() {
        return new JSONObject();
    }
}
