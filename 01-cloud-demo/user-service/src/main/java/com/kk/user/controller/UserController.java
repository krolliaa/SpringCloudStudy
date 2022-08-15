package com.kk.user.controller;

import com.kk.user.coniguration.MyConfigurationProperties;
import com.kk.user.pojo.User;
import com.kk.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/user")
@EnableConfigurationProperties(value = {MyConfigurationProperties.class})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MyConfigurationProperties myConfigurationProperties;

    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }

    @GetMapping("/testShare")
    public Object getShare() {
        return myConfigurationProperties;
    }
}
