package com.example.CarRental.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    private String ratingForRenter;
    private String messageForRenter;
    private String ratingForOwner;
    private String messageForOwner;
}
