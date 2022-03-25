package com.zwm.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.zwm.order.pojo.Order;
import com.zwm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/getAllOrders", method = RequestMethod.GET)
    public String getAllOrders() {
        JSONObject jsonObject = new JSONObject();
        List<Order> orderList = orderService.findAllOrders();
        for (Order order : orderList) {
            Long id = order.getId();
            jsonObject.put("order-" + id, order.toString());
        }
        return jsonObject.toString();
    }
}
