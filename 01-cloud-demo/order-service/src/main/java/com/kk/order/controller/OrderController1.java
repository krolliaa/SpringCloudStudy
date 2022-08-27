package com.kk.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.kk.order.pojo.Order;
import com.kk.order.service.OrderService;
import com.kk.order.service.OrderServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController1 {
    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Autowired
    private OrderService orderService;

    @SentinelResource(value = "hot")
    @GetMapping(value = "/{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        return orderServiceFeign.queryOrderById(orderId);
    }

    @GetMapping(value = "/query")
    public String query() {
        return orderService.queryGoods() + " 查询订单成功！";
    }

    @GetMapping(value = "/save")
    public String save() {
        return orderService.queryGoods() + " 存储订单成功！";
    }

    @GetMapping(value = "/update")
    public String update() {
        return "修改订单成功！";
    }
}