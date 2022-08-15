package com.kk.order.controller;

import com.kk.order.configuration.MyConfigurationProperties;
import com.kk.order.pojo.Order;
import com.kk.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/order")
//@RefreshScope
@EnableConfigurationProperties(value = {MyConfigurationProperties.class})
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MyConfigurationProperties myConfigurationProperties;

    @GetMapping(value = "/{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        return orderService.queryOrderById(orderId);
    }

    //@Value(value = "${pattern.dateformat}")
    //private String patternFormat;

    @GetMapping(value = "/now")
    public String getNow() {
        //return LocalDateTime.now().format(DateTimeFormatter.ofPattern(patternFormat));
        return "";
    }

    @GetMapping(value = "/now2")
    public String getNow2() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(myConfigurationProperties.getDateformat()));
    }

    @GetMapping(value = "/testShare")
    public Object getShare() {
        return myConfigurationProperties;
    }
}