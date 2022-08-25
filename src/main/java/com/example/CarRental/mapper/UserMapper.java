package com.example.CarRental.mapper;

import com.example.CarRental.model.Owner;
import com.example.CarRental.model.Renter;
import com.example.CarRental.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "birthDate", dateFormat= "dd/MM/yyyy")
    Owner mapToOwnerEntity(RegisterRequest request);

    @Mapping(target = "birthDate", dateFormat= "dd/MM/yyyy")
    Renter mapToRenterEntity(RegisterRequest request);
}
