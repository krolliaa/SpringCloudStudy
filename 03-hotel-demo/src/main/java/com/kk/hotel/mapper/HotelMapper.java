package com.kk.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kk.hotel.pojo.Hotel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelMapper extends BaseMapper<Hotel> {
}
