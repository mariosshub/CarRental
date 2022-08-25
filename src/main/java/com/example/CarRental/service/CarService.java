package com.example.CarRental.service;

import com.example.CarRental.exception.SpringException;
import com.example.CarRental.model.Car;
import com.example.CarRental.model.Image;
import com.example.CarRental.model.Owner;
import com.example.CarRental.repository.CarRepository;
import com.example.CarRental.repository.OwnerRepository;
import com.example.CarRental.response.CarEditResp;
import com.example.CarRental.request.CarRequest;
import com.example.CarRental.response.CarResp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CarService {

    private final OwnerRepository ownerRepository;
    private final CarRepository carRepository;
    private final ImageService imageService;


    public Long addCar(CarRequest request, String username) {
        Owner owner = ownerRepository.findByUsername(username)
                .orElseThrow(() -> new SpringException("Owner not found with username " + username));

        Car car = new Car();
        car.setCompany(WordUtils.capitalizeFully(request.getCompany()));
        car.setModel(WordUtils.capitalizeFully(request.getModel()));
        car.setOwner(owner);
        car.setAge(request.getAge());
        car.setCc(request.getCc());
        car.setPrice(request.getPrice());
        car.setLocation(request.getLocation());
        car.setCity(request.getCity());
        car.setAvailableFrom(request.getAvailableFrom());
        car.setAvailableUntil(request.getAvailableUntil());

        return carRepository.save(car).getId();
    }

    public List<CarResp> getOwnersCars(String username) {
        List<Car> cars = carRepository.findByOwner_Username(username);
        List<CarResp> carList = new ArrayList<>();
        for (Car car : cars) {
            String encodedImg  = "";

            if (!car.getImages().isEmpty()) {
                byte[] getFirstImage = car.getImages().get(0).getBytes();
                byte[] encoded = Base64.getEncoder().encode(imageService.decompressBytes(getFirstImage));
                encodedImg = new String(encoded, StandardCharsets.UTF_8);
            }
            carList.add(new CarResp(
                    car.getId(), car.getModel(), car.getCompany(),
                    car.getAge(), car.getCc(), car.getPrice(), car.isAvailable(),
                    car.getLocation(), car.getCity(), encodedImg, car.getAvailableFrom(), car.getAvailableUntil())
            );
        }
        return carList;
    }

    public List<CarResp> getRenterCars(){
        List<Car> cars = carRepository.findByAvailable(true);
        List<CarResp> carList = new ArrayList<>();

        for (Car car : cars) {
            String encodedImg = "";
            if (!car.getImages().isEmpty()){
                byte[] getFirstImage = car.getImages().get(0).getBytes();
                byte[] encoded = Base64.getEncoder().encode(imageService.decompressBytes(getFirstImage)); // get first image
                encodedImg = new String(encoded, StandardCharsets.UTF_8);
            }
            carList.add(new CarResp(
                    car.getId(), car.getModel(), car.getCompany(),
                    car.getAge(), car.getCc(), car.getPrice(), car.isAvailable(),
                    encodedImg, car.getOwner().getUsername())
            );
        }
        return carList;
    }

    public CarEditResp getOneOwnerCar(Long carId){

        Car car = carRepository.findById(carId).orElseThrow(() -> new SpringException("not found"));
        CarEditResp carResp = new CarEditResp();

        carResp.setId(car.getId());
        carResp.setModel(car.getModel());
        carResp.setCompany(car.getCompany());
        carResp.setAge(car.getAge());
        carResp.setCc(car.getCc());
        carResp.setPrice(car.getPrice());
        carResp.setLocation(car.getLocation());
        carResp.setCity(car.getCity());
        carResp.setAvailableFrom(car.getAvailableFrom());
        carResp.setAvailableUntil(car.getAvailableUntil());

        return carResp;
    }

    public CarResp getOne(Long carId){
        Car car = carRepository.findById(carId).orElseThrow(() -> new SpringException("not found"));
        List<String> images = new ArrayList<>();

        if(!car.getImages().isEmpty()){
            for (Image img:car.getImages()) {
                byte[] encoded = Base64.getEncoder().encode(imageService.decompressBytes(img.getBytes()));
                images.add(new String(encoded, StandardCharsets.UTF_8));
            }
        }

        return new CarResp(
                car.getId(), car.getModel() ,car.getCompany(),
                car.getAge(), car.getCc(), car.getPrice(), car.isAvailable(),
                car.getLocation() ,car.getCity(), images, car.getAvailableFrom(), car.getAvailableUntil());
    }

    public void editCar(CarEditResp carResp){
        Car car = carRepository.findById(carResp.getId()).orElseThrow(() -> new SpringException("not found"));

        car.setCompany(carResp.getCompany());
        car.setModel(carResp.getModel());
        car.setAge(carResp.getAge());
        car.setCc(carResp.getCc());
        car.setPrice(carResp.getPrice());
        car.setLocation(carResp.getLocation());
        car.setCity(carResp.getCity());
        car.setAvailableFrom(carResp.getAvailableFrom());
        car.setAvailableUntil(carResp.getAvailableUntil());

        carRepository.save(car);

    }

    public void deleteCarById(Long id){
        imageService.deleteImage(id);
        carRepository.deleteById(id);
    }
}
