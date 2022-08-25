package com.example.CarRental.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String company;
    private Integer age;
    private Integer cc;
    private Integer price;
    private String location;
    private String city;
    private String availableFrom;
    private String availableUntil;

    private boolean available = true;

    @ManyToOne(targetEntity = Owner.class, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id",
            updatable = false,
            foreignKey = @ForeignKey(name = "owner_id_fkey"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // for edit modal and edit js in ownersCars.html (it was throwing an error when edit was pressed)
    private Owner owner;

    @OneToMany(mappedBy = "car")
    @JsonManagedReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Image> images;
}
