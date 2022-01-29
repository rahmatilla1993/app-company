package com.example.appcompany.controller;

import com.example.appcompany.entity.Worker;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.Result;
import com.example.appcompany.models.WorkerDTO;
import com.example.appcompany.service.WorkerService;
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
@RequestMapping("/api/worker")
public class WorkerController {

    ElementNotFound messageAddress = ElementNotFound.ADDRESS;
    ElementNotFound messageDepartment = ElementNotFound.DEPARTMENT;
    ElementNotFound messageWorker = ElementNotFound.WORKER;

    @Autowired
    WorkerService workerService;

    @GetMapping
    public HttpEntity<List<Worker>> getAllWorkers() {
        List<Worker> allWorkers = workerService.getAllWorkers();
        return ResponseEntity.status(HttpStatus.OK).body(allWorkers);
    }

    @GetMapping("/{id}")
    public HttpEntity<Result> getWorkerById(@PathVariable Integer id) {
        Result result = workerService.getWorkerById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(result);
    }

    @GetMapping("/byDepartmentId/{department_id}")
    public HttpEntity<List<Result>> getWorkersByDepartmentId(@PathVariable Integer department_id) {
        List<Result> results = workerService.getWorkersByDepartmentId(department_id);
        return ResponseEntity.status(results.size() != 1 || results.get(0).isSuccess() ? HttpStatus.OK :
                HttpStatus.NOT_FOUND).body(results);
    }

    @PostMapping
    public HttpEntity<Result> addWorker(@Valid @RequestBody WorkerDTO workerDTO) {
        Result result = workerService.addWorker(workerDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.CREATED : result.getMessage().equals(messageAddress.getMessage()) ||
                result.getMessage().equals(messageDepartment.getMessage()) ? HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<Result> deleteWorkerById(@PathVariable Integer id) {
        Result result = workerService.deleteWorkerById(id);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(result);
    }
    //TODO PutMapping qismini bajarish kerak

    @PutMapping("/{id}")
    public HttpEntity<Result> editWorker(@PathVariable Integer id, @Valid @RequestBody WorkerDTO workerDTO) {
        Result result = workerService.editWorker(id, workerDTO);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : result.getMessage().equals(messageAddress.getMessage()) ||
                result.getMessage().equals(messageDepartment.getMessage()) || result.getMessage().equals(messageWorker.getMessage()) ?
                HttpStatus.NOT_FOUND : HttpStatus.CONFLICT).body(result);
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
