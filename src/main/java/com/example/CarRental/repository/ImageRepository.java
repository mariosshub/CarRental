package com.example.CarRental.repository;

import com.example.CarRental.model.Car;
import com.example.CarRental.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByCar(Car car);
    List<Image> findByCarId(Long id);
    void deleteAllByCar_Id(Long carId);
}
