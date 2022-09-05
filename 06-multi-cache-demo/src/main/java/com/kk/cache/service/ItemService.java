package com.kk.cache.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.cache.pojo.Item;

public interface ItemService extends IService<Item> {
    public abstract void saveItem(Item item);
}
