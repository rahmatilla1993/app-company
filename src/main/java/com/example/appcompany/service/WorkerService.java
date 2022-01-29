package com.example.appcompany.service;

import com.example.appcompany.entity.Address;
import com.example.appcompany.entity.Department;
import com.example.appcompany.entity.Worker;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.Result;
import com.example.appcompany.models.WorkerDTO;
import com.example.appcompany.repository.AddressRepository;
import com.example.appcompany.repository.CompanyRepository;
import com.example.appcompany.repository.DepartmentRepository;
import com.example.appcompany.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    ElementNotFound messageWorker = ElementNotFound.WORKER;
    ElementNotFound messageDepartment = ElementNotFound.DEPARTMENT;
    ElementNotFound messageAddress = ElementNotFound.ADDRESS;

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CompanyRepository companyRepository;

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Result getWorkerById(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        return optionalWorker.map(worker -> new Result(true, worker)).orElseGet(() -> new Result(messageWorker.getMessage(), false));
    }

    public List<Result> getWorkersByDepartmentId(Integer department_id) {
        List<Result> results = new ArrayList<>();
        Optional<Department> optionalDepartment = departmentRepository.findById(department_id);
        if (optionalDepartment.isPresent()) {
            List<Worker> workers = workerRepository.findAllByDepartment_DepartmentId(department_id);
            for (Worker worker : workers) {
                Result result = new Result(true, worker);
                results.add(result);
            }
            return results;
        }
        Result result = new Result(messageDepartment.getMessage(), false);
        results.add(result);
        return results;
    }

    private Result addingWorker(WorkerDTO workerDTO, boolean create, boolean edit, Integer id) {
        Worker worker = new Worker();
        if (create && workerRepository.existsByFirstNameAndLastNameAndPhoneNumber(workerDTO.getFirstName(), workerDTO.getLastName(), workerDTO.getPhoneNumber()) ||
                edit && workerRepository.existsByWorkerIdIsNotAndFirstNameAndLastNameAndPhoneNumber(id, workerDTO.getFirstName(), workerDTO.getLastName(), workerDTO.getPhoneNumber())) {
            return new Result("Bunday ishchi bor", false);
        }
        if (create && workerRepository.existsByPhoneNumber(workerDTO.getPhoneNumber()) ||
                edit && workerRepository.existsByWorkerIdIsNotAndPhoneNumber(id, workerDTO.getPhoneNumber())) {
            return new Result("Bunday telefon nomer bor", false);
        }
        Optional<Department> optionalDepartment = departmentRepository.findById(workerDTO.getDepartment_id());
        if (!optionalDepartment.isPresent()) {
            return new Result(messageDepartment.getMessage(), false);
        }
        Department department = optionalDepartment.get();
        Optional<Address> optionalAddress = addressRepository.findById(workerDTO.getAddress_id());
        if (!optionalAddress.isPresent()) {
            return new Result(messageAddress.getMessage(), false);
        }
        Address address = optionalAddress.get();
        if (create && workerRepository.existsByAddress_AddressId(workerDTO.getAddress_id()) ||
                edit && workerRepository.existsByWorkerIdIsNotAndAddress_AddressId(id, workerDTO.getAddress_id())) {
            return new Result("Bu manzilda boshqa ishchi yashaydi", false);
        }
        if (companyRepository.existsByAddress_AddressId(workerDTO.getAddress_id())) {
            return new Result("Bu manzilda company joylashgan", false);
        }
        worker.setAddress(address);
        worker.setDepartment(department);
        worker.setFirstName(workerDTO.getFirstName());
        worker.setLastName(workerDTO.getLastName());
        worker.setPhoneNumber(workerDTO.getPhoneNumber());
        return new Result(true, worker);
    }

    public Result addWorker(WorkerDTO workerDTO) {
        Result result = addingWorker(workerDTO, true, false, null);
        if (result.isSuccess()) {
            Worker worker = (Worker) result.getObject();
            workerRepository.save(worker);
            return new Result("Worker saqlandi", true);
        }
        return result;
    }

    public Result editWorker(Integer id, WorkerDTO workerDTO) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()) {
            Result result = addingWorker(workerDTO, false, true, id);
            if (result.isSuccess()) {
                Worker editWorker = optionalWorker.get();
                Worker worker = (Worker) result.getObject();
                editWorker.setPhoneNumber(worker.getPhoneNumber());
                editWorker.setDepartment(worker.getDepartment());
                editWorker.setLastName(worker.getLastName());
                editWorker.setFirstName(worker.getFirstName());
                editWorker.setAddress(worker.getAddress());
                workerRepository.save(editWorker);
                return new Result("Ishchi o'zgartirildi", true);
            }
            return result;
        }
        return new Result(messageWorker.getMessage(), false);
    }

    public Result deleteWorkerById(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()) {
            workerRepository.delete(optionalWorker.get());
            return new Result("Ishchi o'chirildi", true);
        }
        return new Result(messageWorker.getMessage(), false);
    }
}
