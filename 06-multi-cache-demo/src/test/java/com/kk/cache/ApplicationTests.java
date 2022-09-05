package com.kk.cache;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.kk.cache.pojo.Item;
import com.kk.cache.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private ItemService itemService;

    @Test
    void contextLoads() throws InterruptedException {
        // 构建 Cache 对象
        Cache<Long, String> cache = Caffeine.newBuilder().maximumSize(1).build();
        String json = JSON.toJSONString(itemService.getById(10001L));
        cache.put(10001L, json);
        // 存储缓存数据
        System.out.println("Item = " + cache.getIfPresent(10001L));
    }
}
