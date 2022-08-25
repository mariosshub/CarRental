package com.example.CarRental.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id",foreignKey = @ForeignKey(name = "transaction_id_fkey"))
    @JsonBackReference
    private Transaction transaction;

    private String ratingForRenter;
    private String messageForRenter;

    private String ratingForOwner;
    private String messageForOwner;



}
