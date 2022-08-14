package com.kk.user.mapper;

import com.kk.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from tb_user where id = #{id}")
    public abstract User findById(Long id);
}
