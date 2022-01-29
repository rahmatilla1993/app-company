package com.example.appcompany.service;

import com.example.appcompany.entity.Address;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.AddressDTO;
import com.example.appcompany.models.Result;
import com.example.appcompany.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    ElementNotFound message = ElementNotFound.ADDRESS;

    @Autowired
    AddressRepository addressRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Result getAddressById(Integer id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            return new Result(true, address);
        }
        return new Result(message.getMessage(), false);
    }

    private Result addingAddress(AddressDTO addressDTO, boolean create, boolean edit, Integer id) {

        Address address = new Address();
        if (create && addressRepository.existsByStreetAndHomeNumber(addressDTO.getStreet(), addressDTO.getHomeNumber()) ||
                edit && addressRepository.existsByAddressIdIsNotAndStreetAndHomeNumber(id, addressDTO.getStreet(), addressDTO.getHomeNumber())) {
            return new Result("Bunday address bor", false);
        }
        address.setStreet(addressDTO.getStreet());
        address.setHomeNumber(addressDTO.getHomeNumber());
        return new Result(true, address);
    }

    public Result addAddress(AddressDTO addressDTO) {
        Result result = addingAddress(addressDTO, true, false, null);
        if (result.isSuccess()) {
            Address address = (Address) result.getObject();
            addressRepository.save(address);
            return new Result("Address saqlandi", true);
        }
        return result;
    }

    public Result editAddress(Integer id, AddressDTO addressDTO) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Result result = addingAddress(addressDTO, false, true, id);
            if (result.isSuccess()) {
                Address editAddress = optionalAddress.get();
                Address address = (Address) result.getObject();
                editAddress.setHomeNumber(address.getHomeNumber());
                editAddress.setStreet(address.getStreet());
                addressRepository.save(editAddress);
                return new Result("Address o'zgartirildi", true);
            }
            return result;
        }
        return new Result(message.getMessage(), false);
    }

    public Result deleteAddress(Integer id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            addressRepository.delete(optionalAddress.get());
            return new Result("Address o'chirildi", true);
        }
        return new Result(message.getMessage(), false);
    }
}
