package com.example.appcompany.controller;

import com.example.appcompany.entity.Department;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.DepartmentDTO;
import com.example.appcompany.models.Result;
import com.example.appcompany.service.DepartmentService;
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
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    ElementNotFound messageCompany = ElementNotFound.COMPANY;

    @GetMapping
    public HttpEntity<List<Department>> getAllDepartments() {
        List<Department> allDepartments = departmentService.getAllDepartments();
        return ResponseEntity.status(HttpStatus.OK).body(allDepartments);
    }

    @GetMapping("/{id}")
    public HttpEntity<Result> getDepartmentById(@PathVariable Integer id) {
        Result result = departmentService.getDepartmentById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/byCompanyId/{company_id}")
    public HttpEntity<List<Result>> getAllDepartmentsByCompanyId(@PathVariable Integer company_id) {
        List<Result> results = departmentService.getAllDepartmentsByCompanyId(company_id);
        return ResponseEntity.status(results.size() != 1 || results.get(0).isSuccess() ?
                HttpStatus.OK : HttpStatus.NOT_FOUND).body(results);
    }

    @PostMapping
    public HttpEntity<Result> addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        Result result = departmentService.addDepartment(departmentDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED :
                result.getMessage().equals(messageCompany.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }

    @PutMapping("/{id}")
    public HttpEntity<Result> editDepartment(@PathVariable Integer id, @Valid @RequestBody DepartmentDTO departmentDTO) {
        Result result = departmentService.editDepartment(id, departmentDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED :
                result.getMessage().equals(messageCompany.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Result> deleteDepartment(@PathVariable Integer id) {
        Result result = departmentService.deleteDepartment(id);
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
