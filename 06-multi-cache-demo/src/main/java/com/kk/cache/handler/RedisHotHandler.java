package com.kk.cache.handler;

import com.alibaba.fastjson.JSON;
import com.kk.cache.pojo.Item;
import com.kk.cache.pojo.ItemStock;
import com.kk.cache.service.ItemService;
import com.kk.cache.service.ItemStockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisHotHandler implements InitializingBean {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemStockService itemStockService;

    //private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception {
        //查询商品信息 ---> 将其放入缓存，合并的事情查询 Redis 缓存的时候就做了，这里不用重复，然后放入 Redis 缓存中
        List<Item> itemList = itemService.list();
        for (Item item : itemList) {
            //转换为 JSON 数据，key 为 item:id，你使用 fastjson 或者 jackson 都可以
            String json = JSON.toJSONString(item);
            // String json = MAPPER.writeValueAsString(item);
            redisTemplate.opsForValue().set("item:id" + item.getId(), json);
        }
        //查询库存信息，放入 Redis 缓存中
        List<ItemStock> itemStocks = itemStockService.list();
        for (ItemStock itemStock : itemStocks) {
            String json = JSON.toJSONString(itemStock);
            redisTemplate.opsForValue().set("item:stock:id" + itemStock.getId(), json);
        }
    }

    public void saveItem(Item item) {
        String json = JSON.toJSONString(item);
        redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
    }

    public void deleteItemById(Long id) {
        redisTemplate.delete("item:id:" + id);
    }
}
