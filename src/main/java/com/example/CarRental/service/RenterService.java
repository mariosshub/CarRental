package com.example.CarRental.service;

import com.example.CarRental.exception.SpringException;
import com.example.CarRental.model.*;
import com.example.CarRental.repository.CarRepository;
import com.example.CarRental.repository.LoginRepository;
import com.example.CarRental.repository.RenterRepository;
import com.example.CarRental.repository.TransactionRepository;
import com.example.CarRental.request.ProfileEditRequest;
import com.example.CarRental.request.RentRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class RenterService {

    private final CarRepository carRepository;
    private final RenterRepository renterRepository;
    private final TransactionRepository transactionRepository;
    private final LoginRepository loginRepository;

    private final PasswordEncoder passwordEncoder;


    public Renter getRenter(String username){
        Renter renter =renterRepository.findByUsername(username).orElse(null);
        return renter;
    }
    public String editProfile(ProfileEditRequest request, String username) {
        Renter renter = renterRepository.findByUsername(username).orElseThrow(() -> new SpringException("not found"));

        renter.setFirstName(request.getFirstName());
        renter.setLastName(request.getLastName());
        if (request.getBirthDate() != null) {
            renter.setBirthDate( LocalDate.parse( request.getBirthDate(), DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) ) );
        }
        renter.setEmail(request.getEmail());
        if(passwordEncoder.matches(request.getPasswordOld(),renter.getPassword())){
            if(!request.getPasswordNew().isBlank()){
                renter.setPassword(passwordEncoder.encode(request.getPasswordNew()));
            }
        }

        LoginDetails login = loginRepository.findByUsername(renter.getUsername()).orElseThrow(() -> new SpringException("not found"));
        login.setUsername(renter.getUsername());
        login.setPassword(renter.getPassword());
        loginRepository.save(login);
        renterRepository.save(renter);
        return "settings_changed";
    }

    public List<Transaction> getRentals(String username){
        List<Transaction> transactions = transactionRepository.findByRenterUsername(username);
        return transactions;
    }

    public String sendCarRentalRequest(String username, Long carId, RentRequest rentRequest) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new SpringException("Car not found with ID " + carId));

        Optional<Transaction> existingTransaction = transactionRepository
                .findByCarAndRenter_UsernameAndStatus(car, username, Status.PENDING);

        String response = "";

        if (existingTransaction.isEmpty()) {
            Owner owner = car.getOwner();
            Renter renter = renterRepository.findByUsername(username)
                    .orElseThrow(() -> new SpringException("No renter found with username " + username));
            Transaction newTransaction  = new Transaction();

            LocalDate lt = LocalDate.now();
            if(ChronoUnit.DAYS.between(lt, rentRequest.getStartDate())< 0){
                response = "invalid_date";
            }
            else{
                if (ChronoUnit.DAYS.between(rentRequest.getStartDate(), rentRequest.getEndDate()) > 0){
                    newTransaction.setCar(car);
                    newTransaction.setRenter(renter);
                    newTransaction.setOwner(owner);
                    newTransaction.setStartDate(rentRequest.getStartDate());
                    newTransaction.setEndDate(rentRequest.getEndDate());
                    int days = (int) ChronoUnit.DAYS.between(rentRequest.getStartDate(), rentRequest.getEndDate());
                    newTransaction.setCost(days*car.getPrice());
                    newTransaction.setStatus(Status.PENDING);
                    transactionRepository.save(newTransaction);
                    response = "successful";
                }
                else
                    response = "days_not_match";

            }
        }
        else
            response = "transaction_exists";
        return response;
    }
}
