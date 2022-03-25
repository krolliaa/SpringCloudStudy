package com.zwm.user.service;

import com.alibaba.fastjson.JSONObject;
import com.zwm.user.mapper.UserMapper;
import com.zwm.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JSONObject jsonObject;

    public String findAllUsers() {
        List<User> userList = userMapper.selectAllUsers();
        for (User user : userList) {
            Long id = user.getId();
            jsonObject.put("user-" + id, user.toString());
        }
        return jsonObject.toString();
    }

    public User findUserById(int id) {
        return userMapper.selectUserById(id);
    }
}
