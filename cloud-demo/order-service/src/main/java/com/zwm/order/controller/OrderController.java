package com.zwm.order.controller;

import com.zwm.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/getAllOrders", method = RequestMethod.GET)
    public Object getAllOrders() {
        return orderService.findAllOrders();
    }
}
