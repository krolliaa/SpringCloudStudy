package com.zwm.user.mapper;

import com.zwm.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    public abstract List<User> selectAllUsers();
    public abstract User selectUserById(int id);
}
