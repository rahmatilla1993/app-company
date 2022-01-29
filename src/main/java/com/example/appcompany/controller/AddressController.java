package com.example.appcompany.controller;

import com.example.appcompany.entity.Address;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.AddressDTO;
import com.example.appcompany.models.Result;
import com.example.appcompany.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    ElementNotFound message = ElementNotFound.ADDRESS;

    @Autowired
    AddressService addressService;

    @GetMapping
    public HttpEntity<List<Address>> getAllAddresses() {
        List<Address> allAddresses = addressService.getAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(allAddresses);
    }

    @GetMapping("/{id}")
    public HttpEntity<Result> getAddressById(@PathVariable Integer id) {
        Result addressById = addressService.getAddressById(id);
        return ResponseEntity.status(addressById.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(addressById);
    }

    @PostMapping
    public HttpEntity<Result> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        Result result = addressService.addAddress(addressDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<Result> editAddress(@PathVariable Integer id, @Valid @RequestBody AddressDTO addressDTO) {
        Result result = addressService.editAddress(id, addressDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED :
                result.getMessage().equals(message.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Integer id){
        Result result = addressService.deleteAddress(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
