package com.zwm.user.service;

import com.zwm.user.mapper.UserMapper;
import com.zwm.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> findAllUsers() {
        return userMapper.selectAllUsers();
    }
}
