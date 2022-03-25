package com.zwm.user.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.zwm.user.pojo.User;
import com.zwm.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public String getAllUsers() {
        JSONObject jsonObject = new JSONObject();
        List<User> userList = userService.findAllUsers();
        for (User user : userList) {
            Long id = user.getId();
            jsonObject.put("user-" + id, user.toString());
        }
        return jsonObject.toString();
    }
}
