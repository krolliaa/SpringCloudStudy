package com.kk.order.service;

import com.kk.order.mapper.OrderMapper;
import com.kk.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public Order queryOrderById(Long id) {
        return orderMapper.findById(id);
    }
}
