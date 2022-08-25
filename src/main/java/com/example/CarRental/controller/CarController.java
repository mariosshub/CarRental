package com.example.CarRental.controller;


import com.example.CarRental.response.CarEditResp;
import com.example.CarRental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/cars")
@AllArgsConstructor
public class CarController {

    private final CarService carService;


    @GetMapping("/getOne")
    @ResponseBody
    public CarEditResp getOne(Long Id){
        return carService.getOneOwnerCar(Id);
    }
}
