package com.kk.hotel.controller;

import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "/{id}")
    @ResponseBody
    public Hotel findById(@PathVariable(value = "id") Long id) {
        return hotelService.getById(id);
    }


}
