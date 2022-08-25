package com.example.CarRental.service;

import com.example.CarRental.model.Review;
import com.example.CarRental.model.Transaction;
import com.example.CarRental.repository.ReviewRepository;
import com.example.CarRental.repository.TransactionRepository;
import com.example.CarRental.request.ReviewRequest;
import com.example.CarRental.response.ReviewResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReviewService {

    private final TransactionRepository transactionRepository;
    private final ReviewRepository reviewRepository;

    public String createReview(ReviewRequest reviewRequest, Long transactionId, boolean owner){
        Transaction tr = transactionRepository.findById(transactionId).orElse(null);
        Optional<Review> review = reviewRepository.findByTransaction(tr);
        if(review.isPresent()){
            if(owner){
                review.get().setMessageForOwner(reviewRequest.getMessageForOwner());
                review.get().setRatingForOwner(reviewRequest.getRatingForOwner());
            }
            else{
                review.get().setMessageForRenter(reviewRequest.getMessageForRenter());
                review.get().setRatingForRenter(reviewRequest.getRatingForRenter());
            }
            reviewRepository.save(review.get());
        }
        // create a new review
        else{
            Review newReview = new Review();
            if(owner){
                newReview.setMessageForOwner(reviewRequest.getMessageForOwner());
                newReview.setRatingForOwner(reviewRequest.getRatingForOwner());
            }
            else{
                newReview.setMessageForRenter(reviewRequest.getMessageForRenter());
                newReview.setRatingForRenter(reviewRequest.getRatingForRenter());
            }
            newReview.setTransaction(tr);
            reviewRepository.save(newReview);
        }
        return "review_sent";
    }

    public ReviewResponse seeOwnersFromRenterReview(Long transactionId){
        Review review = reviewRepository.findByTransactionId(transactionId).orElse(null);
        ReviewResponse reviewResponse = new ReviewResponse();
        if(review!=null){
            reviewResponse.setRating(review.getRatingForRenter());
            reviewResponse.setMessage(review.getMessageForRenter());
        }
        return reviewResponse;
    }

    public ReviewResponse seeRentersFromOwnerReview(Long transactionId){
        Review review = reviewRepository.findByTransactionId(transactionId).orElse(null);
        ReviewResponse reviewResponse = new ReviewResponse();
        if(review!=null){
            reviewResponse.setRating(review.getRatingForOwner());
            reviewResponse.setMessage(review.getMessageForOwner());
        }
        return reviewResponse;
    }

}
