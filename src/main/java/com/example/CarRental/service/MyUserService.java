package com.example.CarRental.service;

import com.example.CarRental.model.LoginDetails;
import com.example.CarRental.model.Role;
import com.example.CarRental.repository.LoginRepository;
import com.example.CarRental.repository.OwnerRepository;
import com.example.CarRental.repository.RenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MyUserService implements UserDetailsService {

    private final LoginRepository loginRepository;
    private final OwnerRepository ownerRepository;
    private final RenterRepository renterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDetails details = loginRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with username " + username));

        if (details.getRole().equals(Role.OWNER))
            return ownerRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No user with username " + username));
        else
            return renterRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No user with username " + username));
    }
}
