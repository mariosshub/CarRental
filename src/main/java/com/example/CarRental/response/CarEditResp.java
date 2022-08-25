package com.example.CarRental.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarEditResp {

    private Long id;
    private String model;
    private String company;
    private Integer age;
    private Integer cc;
    private Integer price;
    private String location;
    private String city;
    private String availableFrom;
    private String availableUntil;
}
