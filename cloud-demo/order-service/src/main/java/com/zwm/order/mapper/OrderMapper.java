package com.zwm.order.mapper;

import com.zwm.order.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    //查询所有订单
    public abstract List<Order> selectAllOrders();
}
