package com.example.CarRental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "car_id",foreignKey = @ForeignKey(name ="car_id_fkey"))
    private Car car;

    @OneToOne
    @JoinColumn(name = "renter_id",foreignKey = @ForeignKey(name ="renter_id_fkey"))
    private Renter renter;

    @OneToOne
    @JoinColumn(name="owner_id",foreignKey = @ForeignKey(name = "owner_id_fkey"))
    private Owner owner;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private Integer cost;

    @OneToOne(mappedBy = "transaction")
    private Review review ;




}
