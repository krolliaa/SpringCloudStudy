package com.zwm.user.controller;

import com.zwm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public String getAllUsers() {
        return userService.findAllUsers();
    }

    @ResponseBody
    @RequestMapping(value = "/getUserById", method = RequestMethod.GET)
    public Object getUserById(int id) {
        return userService.findUserById(id);
    }
}
