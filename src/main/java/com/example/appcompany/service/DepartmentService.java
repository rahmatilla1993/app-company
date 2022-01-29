package com.example.appcompany.service;

import com.example.appcompany.entity.Company;
import com.example.appcompany.entity.Department;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.DepartmentDTO;
import com.example.appcompany.models.Result;
import com.example.appcompany.repository.CompanyRepository;
import com.example.appcompany.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    ElementNotFound messageDepartment = ElementNotFound.DEPARTMENT;
    ElementNotFound messageCompany = ElementNotFound.COMPANY;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CompanyRepository companyRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Result getDepartmentById(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        return optionalDepartment.map(department -> new Result(true, department)).orElseGet(() -> new Result(messageDepartment.getMessage(), false));
    }

    public List<Result> getAllDepartmentsByCompanyId(Integer company_id) {
        List<Result> results = new ArrayList<>();
        Optional<Company> optionalCompany = companyRepository.findById(company_id);
        if (optionalCompany.isPresent()) {
            List<Department> departments = departmentRepository.findAllByCompany_CompanyId(company_id);
            for (Department department : departments) {
                Result result = new Result(true, department);
                results.add(result);
            }
            return results;
        }
        Result result = new Result(messageCompany.getMessage(), false);
        results.add(result);
        return results;
    }

    private Result addingDepartment(DepartmentDTO departmentDTO, boolean create, boolean edit, Integer id) {
        Department department = new Department();
        Optional<Company> optionalCompany = companyRepository.findById(departmentDTO.getCompany_id());
        if (!optionalCompany.isPresent()) {
            return new Result(messageCompany.getMessage(), false);
        }
        Company company = optionalCompany.get();
        if (create && departmentRepository.existsByNameAndCompany_CompanyId(departmentDTO.getName(), departmentDTO.getCompany_id()) ||
                edit && departmentRepository.existsByDepartmentIdIsNotAndNameAndCompany_CompanyId(id, departmentDTO.getName(), departmentDTO.getCompany_id())) {
            return new Result("Bunday bo'lim shu kompaniyada bor", false);
        }
        department.setCompany(company);
        department.setName(departmentDTO.getName());
        return new Result(true, department);
    }

    public Result addDepartment(DepartmentDTO departmentDTO) {
        Result result = addingDepartment(departmentDTO, true, false, null);
        if (result.isSuccess()) {
            Department department = (Department) result.getObject();
            departmentRepository.save(department);
            return new Result("Bo'lim saqlandi", true);
        }
        return result;
    }

    public Result editDepartment(Integer id, DepartmentDTO departmentDTO) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            Result result = addingDepartment(departmentDTO, false, true, id);
            if (result.isSuccess()) {
                Department editDepartment = optionalDepartment.get();
                Department department = (Department) result.getObject();
                editDepartment.setName(department.getName());
                editDepartment.setCompany(department.getCompany());
                departmentRepository.save(editDepartment);
                return new Result("Bo'lim o'zgartirildi", true);
            }
            return result;
        }
        return new Result(messageDepartment.getMessage(), false);
    }

    public Result deleteDepartment(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            departmentRepository.delete(optionalDepartment.get());
            return new Result("Bo'lim o'chirildi", true);
        }
        return new Result(messageDepartment.getMessage(), false);
    }
}
