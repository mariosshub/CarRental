package com.example.CarRental.mapper;

import com.example.CarRental.model.LoginDetails;
import com.example.CarRental.request.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper
public interface LoginMapper {

    LoginDetails mapToLogin(RegisterRequest request);
}
