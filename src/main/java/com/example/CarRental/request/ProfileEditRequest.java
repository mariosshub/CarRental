package com.example.CarRental.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEditRequest {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private String passwordOld;
    private String passwordNew;
    private String role;
}
