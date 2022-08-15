package com.kk.user.controller;

import com.kk.user.pojo.User;
import com.kk.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController2 {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}")
    public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "sign", required = false) String sign) {
        log.warn(sign);
        return userService.queryById(id);
    }
}
