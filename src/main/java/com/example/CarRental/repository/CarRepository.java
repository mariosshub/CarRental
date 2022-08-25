package com.example.CarRental.repository;

import com.example.CarRental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwner_Username(String username);
    List<Car> findByAvailable(boolean available);
}
