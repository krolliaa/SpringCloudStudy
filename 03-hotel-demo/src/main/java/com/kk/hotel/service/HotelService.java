package com.kk.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.pojo.PageResult;
import com.kk.hotel.pojo.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HotelService extends IService<Hotel>  {
    PageResult search(RequestParam requestParam) throws IOException;
    Map<String, List<String>> filters(RequestParam requestParam);
}
