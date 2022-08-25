package com.example.CarRental.service;

import com.example.CarRental.mapper.LoginMapper;
import com.example.CarRental.mapper.UserMapper;
import com.example.CarRental.model.LoginDetails;
import com.example.CarRental.model.Role;
import com.example.CarRental.repository.LoginRepository;
import com.example.CarRental.repository.OwnerRepository;
import com.example.CarRental.repository.RenterRepository;
import com.example.CarRental.request.RegisterRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

//    Repositories
    private final OwnerRepository ownerRepository;
    private final RenterRepository renterRepository;
    private final LoginRepository loginRepository;

//    Mappers
    private final UserMapper userMapper;
    private final LoginMapper loginMapper;

//    Security
    private final PasswordEncoder passwordEncoder;


    public boolean signup(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setFirstName(WordUtils.capitalizeFully(request.getFirstName()));
        request.setLastName(WordUtils.capitalizeFully(request.getLastName()));
        request.setPassword(encodedPassword);

        LoginDetails details = loginMapper.mapToLogin(request);
        loginRepository.save(details);

        if (request.getRole().equals(Role.OWNER)) {
            ownerRepository.save(userMapper.mapToOwnerEntity(request));
        }
        else {
            renterRepository.save(userMapper.mapToRenterEntity(request));
        }
        return true;
    }
}

