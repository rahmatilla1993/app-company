package com.example.appcompany.repository;

import com.example.appcompany.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {

    boolean existsByStreetAndHomeNumber(String street, Integer homeNumber);

    boolean existsByAddressIdIsNotAndStreetAndHomeNumber(Integer addressId, String street, Integer homeNumber);
}
