package com.kk.cache.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.cache.mapper.ItemMapper;
import com.kk.cache.pojo.Item;
import com.kk.cache.pojo.ItemStock;
import com.kk.cache.service.ItemService;
import com.kk.cache.service.ItemStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    private ItemStockService itemStockService;

    @Override
    @Transactional
    public void saveItem(Item item) {
        //存放商品
        this.save(item);
        //存放商品的同时需要存放商品库存 ---> 跟 pojo 对应上
        ItemStock itemStock = new ItemStock();
        itemStock.setId(item.getId());
        itemStock.setStock(item.getStock());
        itemStock.setSold(item.getSold());
        itemStockService.save(itemStock);
    }
}
