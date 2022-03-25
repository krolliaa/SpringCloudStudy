package com.zwm.order.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zwm.order.mapper.OrderMapper;
import com.zwm.order.pojo.Order;
import com.zwm.order.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private JSONObject jsonObject;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> findAllOrders() {
        List<Order> orderList = orderMapper.selectAllOrders();
        for (Order order : orderList) {
            String url = "http://localhost:8081/getUserById?id=" + order.getUserId();
            order.setUser(restTemplate.getForObject(url, User.class));
        }
        return orderList;
    }
}
