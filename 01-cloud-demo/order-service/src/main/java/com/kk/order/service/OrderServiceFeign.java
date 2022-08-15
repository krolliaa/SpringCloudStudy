package com.kk.order.service;

import com.kk.clients.UserClient;
import com.kk.order.mapper.OrderMapper;
import com.kk.order.pojo.Order;
import com.kk.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceFeign {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long id) {
        Order order = orderMapper.findById(id);
        User user = userClient.findById(order.getUserId());
        order.setUser(user);
        return order;
    }
}
