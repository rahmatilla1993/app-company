package com.example.appcompany.controller;

import com.example.appcompany.entity.Company;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.CompanyDTO;
import com.example.appcompany.models.Result;
import com.example.appcompany.service.CompanyService;
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
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    ElementNotFound messageCompany = ElementNotFound.COMPANY;
    ElementNotFound messageAddress = ElementNotFound.ADDRESS;

    @GetMapping
    public HttpEntity<List<Company>> getAllCompanies() {
        List<Company> allCompanies = companyService.getAllCompanies();
        return ResponseEntity.status(HttpStatus.OK).body(allCompanies);
    }

    @GetMapping("/{id}")
    public HttpEntity<Result> getCompanyById(@PathVariable Integer id) {
        Result result = companyService.getCompanyById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(result);
    }

    @PostMapping
    public HttpEntity<Result> addCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        Result result = companyService.addCompany(companyDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<Result> editCompany(@PathVariable Integer id, @Valid @RequestBody CompanyDTO companyDTO) {
        Result result = companyService.editCompany(id, companyDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageCompany.getMessage()) ||
                result.getMessage().equals(messageAddress.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Result> deleteCompany(@PathVariable Integer id) {
        Result result = companyService.deleteCompany(id);
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
