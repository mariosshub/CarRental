package com.example.CarRental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    public LoginDetails(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Enumerated(value = EnumType.STRING)
    private Role role;
}
