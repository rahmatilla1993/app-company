package com.example.appcompany.repository;

import com.example.appcompany.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Integer> {

    List<Worker> findAllByDepartment_DepartmentId(Integer department_departmentId);

    boolean existsByFirstNameAndLastNameAndPhoneNumber(String firstName, String lastName, String phoneNumber);

    boolean existsByWorkerIdIsNotAndFirstNameAndLastNameAndPhoneNumber(Integer workerId, String firstName, String lastName, String phoneNumber);

    boolean existsByAddress_AddressId(Integer address_addressId);

    boolean existsByWorkerIdIsNotAndAddress_AddressId(Integer workerId, Integer address_addressId);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByWorkerIdIsNotAndPhoneNumber(Integer workerId, String phoneNumber);
}
