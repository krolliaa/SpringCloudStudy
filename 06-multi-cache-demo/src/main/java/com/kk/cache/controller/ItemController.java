package com.kk.cache.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import com.kk.cache.pojo.Item;
import com.kk.cache.pojo.ItemStock;
import com.kk.cache.pojo.PageDTO;
import com.kk.cache.service.ItemService;
import com.kk.cache.service.ItemStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemStockService itemStockService;

    @Autowired
    private Cache<Long, Item> itemCache;

    @Autowired
    private Cache<Long, ItemStock> itemStockCache;

    /**
     * 使用 MyBatisPlus 自带的分页功能分页查询商品
     * 根据前端传递的 page【当前页】 + size【每页显示数量】 获取商品集合，原理就是 (page - 1) * size
     * 这里还是用到了 stream 流操作 + lambda 表达式，这里使用 peek 表示为每个元素添加操作
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/list")
    public PageDTO queryItemPage(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Page<Item> itemPage = itemService.query().ne("status", 3).page(new Page<>(page, size));
        List<Item> list = itemPage.getRecords().stream().peek(item -> {
            ItemStock itemStock = itemStockService.getById(item.getId());
            if (itemStock != null) {
                item.setStock(itemStock.getStock());
                item.setSold(itemStock.getSold());
            } else {
                item.setStock(0);
                item.setSold(0);
            }
        }).collect(Collectors.toList());
        Long total = itemPage.getTotal();
        return new PageDTO(total, list);
    }


    /**
     * 获取指定的商品信息
     * 需要先判断下商品是否已经被删除
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Item findById(@PathVariable("id") Long id) {
        //获取缓存中 itemStock，没有的话就从数据库中查询，然后存储在本地
        ItemStock itemStock = findStockById(id);
        //获取缓存中的 item，没有的话就从数据库中查询，然后存储在本地
        Item item = itemCache.get(id, key -> {
            Item itemDatabase = itemService.query().ne("status", 3).eq("id", id).one();
            //存储在本地缓存
            itemCache.put(key, itemDatabase);
            return itemDatabase;
        });
        if (item != null) {
            item.setStock(itemStock.getStock());
            item.setSold(itemStock.getSold());
        }
        return item;
    }

    /**
     * 存储商品信息
     *
     * @param item
     */
    @PostMapping
    public void saveItem(@RequestBody Item item) {
        itemService.saveItem(item);
    }

    /**
     * 更新商品信息
     *
     * @param item
     */
    @PutMapping
    public void updateItem(@RequestBody Item item) {
        itemService.updateById(item);
    }

    /**
     * 删除商品信息
     * 不是真的物理删除，而是逻辑删除，更新状态 3 意为删除
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        itemService.update().set("status", 3).eq("id", id).update();
    }

    /**
     * 更新商品库存信息
     *
     * @param itemStock
     */
    @PutMapping(value = "/stock")
    public void updateStock(@RequestBody ItemStock itemStock) {
        itemStockService.updateById(itemStock);
    }


    /**
     * 查询指定的商品库存信息
     *
     * @param id
     * @return
     */
    @GetMapping("/stock/{id}")
    public ItemStock findStockById(@PathVariable("id") Long id) {
        return itemStockCache.get(id, key -> {
            ItemStock itemStockDataBase = itemStockService.getById(key);
            itemStockCache.put(key, itemStockDataBase);
            //如果查询到的数据为 null，则新建对象返回
            return itemStockDataBase == null ? new ItemStock(id, 0, 0) : itemStockDataBase;
        });
    }

}
