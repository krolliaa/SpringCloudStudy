package com.zwm.order.service;

import com.zwm.order.mapper.OrderMapper;
import com.zwm.order.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public List<Order> findAllOrders() {
        return orderMapper.selectAllOrders();
    }
}
