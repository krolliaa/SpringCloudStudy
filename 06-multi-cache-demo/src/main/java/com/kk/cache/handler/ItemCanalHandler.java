package com.kk.cache.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.kk.cache.handler.RedisHotHandler;
import com.kk.cache.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@Component
public class ItemCanalHandler implements EntryHandler<Item> {
    @Autowired
    private RedisHotHandler redisHandler;
    @Autowired
    private Cache<Long, Item> itemCache;

    @Override
    public void update(Item before, Item after) {
        // 写数据到JVM进程缓存
        itemCache.put(after.getId(), after);
        // 写数据到redis
        redisHandler.saveItem(after);
    }

    @Override
    public void delete(Item item) {
        // 删除数据到JVM进程缓存
        itemCache.invalidate(item.getId());
        // 删除数据到redis
        redisHandler.deleteItemById(item.getId());
    }
}
