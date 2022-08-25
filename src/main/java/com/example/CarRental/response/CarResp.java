package com.example.CarRental.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@NoArgsConstructor
public class CarResp {

    private Long id;
    private String model;
    private String company;
    private Integer age;
    private Integer cc;
    private Integer price;
    private String location;
    private String city;
    private boolean available;

    private String availableFrom;
    private String availableUntil;

    private List<String> images;
    private String image;

    private String owner;

    public CarResp(Long id, String model, String company,Integer age,
                   Integer cc, Integer price, boolean available,
                   String location, String city, List<String> images, String availableFrom, String availableUntil) {
        this.id = id;
        this.model = model;
        this.company = company;
        this.age = age;
        this.cc = cc;
        this.price =price;
        this.available = available;
        this.location = location;
        this.city = city;
        this.images = images;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;

    }

    public CarResp(Long id, String model, String company, Integer age,
                   Integer cc, Integer price,
                   boolean available, String image, String owner) {
        this.id = id;
        this.model = model;
        this.company = company;
        this.age = age;
        this.cc = cc;
        this.price= price;
        this.available = available;
        this.image = image;
        this.owner = owner;
    }
    public CarResp(Long id, String model, String company, Integer age,
                   Integer cc, Integer price, boolean available, String location,
                   String city, String image, String availableFrom, String availableUntil) {
        this.id = id;
        this.model = model;
        this.company = company;
        this.age = age;
        this.cc = cc;
        this.price= price;
        this.available = available;
        this.location = location;
        this.city = city;
        this.image = image;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
    }
}
