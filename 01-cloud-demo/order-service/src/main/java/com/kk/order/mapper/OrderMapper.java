package com.kk.order.mapper;

import com.kk.order.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {
    @Select("select * from tb_order where id = #{id}")
    public abstract Order findById(Long id);
}
