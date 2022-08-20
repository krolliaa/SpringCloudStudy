package com.kk.hotel.controller;

import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.pojo.PageResult;
import com.kk.hotel.pojo.RequestParam;
import com.kk.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "/{id}")
    public Hotel findById(@PathVariable(value = "id") Long id) {
        return hotelService.getById(id);
    }

    @PostMapping(value = "/list")
    public PageResult search(@RequestBody RequestParam requestParam) throws IOException {
        return hotelService.search(requestParam);
    }
}
