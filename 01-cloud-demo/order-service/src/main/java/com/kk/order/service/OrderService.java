package com.kk.order.service;

import com.kk.order.mapper.OrderMapper;
import com.kk.order.pojo.Order;
import com.kk.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    public Order queryOrderById(Long id) {
        Order order = orderMapper.findById(id);
        String url = "http://user-service/user/" + order.getUserId();
        User user = restTemplate.getForObject(url, User.class);
        order.setUser(user);
        System.out.println(user);
        return order;
    }
}
