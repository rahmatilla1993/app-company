package com.example.appcompany.repository;

import com.example.appcompany.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Integer> {

    boolean existsByCorpName(String corpName);

    boolean existsByCompanyIdIsNotAndCorpName(Integer companyId, String corpName);

    boolean existsByAddress_AddressId(Integer address_addressId);

    boolean existsByCompanyIdIsNotAndAddress_AddressId(Integer companyId, Integer address_addressId);
}
