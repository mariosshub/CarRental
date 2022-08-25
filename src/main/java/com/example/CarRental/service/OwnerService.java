package com.example.CarRental.service;

import com.example.CarRental.exception.SpringException;
import com.example.CarRental.model.*;
import com.example.CarRental.repository.CarRepository;
import com.example.CarRental.repository.LoginRepository;
import com.example.CarRental.repository.OwnerRepository;
import com.example.CarRental.repository.TransactionRepository;
import com.example.CarRental.request.ProfileEditRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final CarRepository carRepository;
    private final TransactionRepository transactionRepository;
    private final LoginRepository loginRepository;
    //    Security
    private final PasswordEncoder passwordEncoder;



    public Owner getOwner(String username){
        return ownerRepository.findByUsername(username)
                .orElseThrow(() -> new SpringException("Owner not found with username" + username));
    }

    public String editProfile(ProfileEditRequest request, String username){
        Owner owner = ownerRepository.findByUsername(username).orElseThrow(() -> new SpringException("not found"));

        owner.setFirstName(request.getFirstName());
        owner.setLastName(request.getLastName());
        if ( request.getBirthDate() != null )
            owner.setBirthDate( LocalDate.parse( request.getBirthDate(), DateTimeFormatter.ofPattern( "dd/MM/yyyy" ) ) );

        owner.setEmail(request.getEmail());
        if (passwordEncoder.matches(request.getPasswordOld(), owner.getPassword()))
            if(!request.getPasswordNew().isBlank())
                owner.setPassword(passwordEncoder.encode(request.getPasswordNew()));

        LoginDetails login = loginRepository.findByUsername(owner.getUsername())
                .orElseThrow(() -> new SpringException("not found"));
        login.setUsername(owner.getUsername());
        login.setPassword(owner.getPassword());
        loginRepository.save(login);
        ownerRepository.save(owner);
        return "settings_changed";
    }

    public List<Transaction> getTransactions(String username){
        List<Transaction> transactions = transactionRepository.findByOwner_Username(username);
        return transactions;
    }

    public String completeTransaction(Long transactionId) {
        Optional<Transaction> activeTransaction = transactionRepository.findById(transactionId);

        if (activeTransaction.isPresent()) {
            Status state = activeTransaction.get().getStatus();
            switch (state) {
                case ACCEPTED:
                    activeTransaction.get().setStatus(Status.COMPLETED);
                    transactionRepository.save(activeTransaction.get());

                    Long carId = activeTransaction.get().getCar().getId();
                    Car car = carRepository.findById(carId)
                            .orElseThrow(() -> new SpringException("Car not found with ID " + carId));
                    car.setAvailable(true);
                    carRepository.save(car);

                    return "completed";
                case PENDING:
                    return "accept_first";
                case COMPLETED:
                    return "already_completed";
            }
        }
        return null;
    }

    public String acceptTransaction(Long transactionId) {
        Optional<Transaction> pendingTransaction = transactionRepository.findById(transactionId);

        if (pendingTransaction.isPresent()) {
            Transaction transaction = pendingTransaction.get();
            Status transactionState = transaction.getStatus();

            switch (transactionState) {
                case PENDING:
                    // TODO pending transactions of the same renter for any other owners car must be deleted
                    transaction.setStatus(Status.ACCEPTED);
                    transactionRepository.save(transaction);

                    Car car = transaction.getCar();
                    car.setAvailable(false);
                    carRepository.save(car);

                    return "accepted";
                case ACCEPTED:
                    return "already_accepted";
                case COMPLETED:
                    return "is_completed";
            }
        }
        return null;
    }


}
