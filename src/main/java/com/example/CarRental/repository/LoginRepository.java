package com.example.CarRental.repository;

import com.example.CarRental.model.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginDetails, Long> {

    Optional<LoginDetails> findByUsername(String username);
}
