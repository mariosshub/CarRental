package com.example.CarRental.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {

    private String model;
    private String company;
    private Integer age;
    private Integer cc;
    private Integer price;
    private String location;
    private String city;
    private List<MultipartFile> images;

    private String availableFrom;
    private String availableUntil;

}
