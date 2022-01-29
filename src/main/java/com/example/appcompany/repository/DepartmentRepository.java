package com.example.appcompany.repository;

import com.example.appcompany.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    List<Department> findAllByCompany_CompanyId(Integer company_companyId);

    boolean existsByNameAndCompany_CompanyId(String name, Integer company_companyId);

    boolean existsByDepartmentIdIsNotAndNameAndCompany_CompanyId(Integer departmentId, String name, Integer company_companyId);
}
