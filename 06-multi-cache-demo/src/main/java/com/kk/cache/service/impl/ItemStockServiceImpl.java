package com.kk.cache.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.cache.mapper.ItemStockMapper;
import com.kk.cache.pojo.ItemStock;
import com.kk.cache.service.ItemStockService;
import org.springframework.stereotype.Service;

@Service
public class ItemStockServiceImpl extends ServiceImpl<ItemStockMapper, ItemStock> implements ItemStockService {
}