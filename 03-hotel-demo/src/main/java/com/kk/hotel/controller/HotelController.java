package com.kk.hotel.controller;

import com.kk.hotel.pojo.Hotel;
import com.kk.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "/{id}")
    public Hotel findById(@PathVariable(value = "id") Long id) {
        return hotelService.getById(id);
    }
}
