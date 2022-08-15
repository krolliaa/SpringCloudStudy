package com.kk.order.client;

import com.kk.order.configuration.MyFeignConfiguration;
import com.kk.order.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service", configuration = {MyFeignConfiguration.class})
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable(value = "id") Long id);
}
