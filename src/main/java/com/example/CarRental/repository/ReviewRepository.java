package com.example.CarRental.repository;

import com.example.CarRental.model.Review;
import com.example.CarRental.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByTransaction(Transaction t);
    Optional<Review> findByTransactionId(Long transactionId);
}
