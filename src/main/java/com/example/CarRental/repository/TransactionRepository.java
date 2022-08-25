package com.example.CarRental.repository;

import com.example.CarRental.model.Car;
import com.example.CarRental.model.Status;
import com.example.CarRental.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByOwner_Username(String username);
    Optional<Transaction> findByCarAndRenter_UsernameAndStatus(Car c, String n, Status s);
//    Optional<Transaction> findByCarAndRenterUsernameAndStatus(Car c, String n, Status s);
    List<Transaction> findByRenterUsername(String username);
}
