package com.example.appcompany.service;

import com.example.appcompany.entity.Address;
import com.example.appcompany.entity.Company;
import com.example.appcompany.enums.ElementNotFound;
import com.example.appcompany.models.CompanyDTO;
import com.example.appcompany.models.Result;
import com.example.appcompany.repository.AddressRepository;
import com.example.appcompany.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    ElementNotFound messageCompany = ElementNotFound.COMPANY;
    ElementNotFound messageAddress = ElementNotFound.ADDRESS;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    AddressRepository addressRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Result getCompanyById(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        return optionalCompany.map(company -> new Result(true, company)).orElseGet(() -> new Result(messageCompany.getMessage(), false));
    }

    private Result addingCompany(CompanyDTO companyDTO, boolean create, boolean edit, Integer id) {
        Company company = new Company();
        if (create && companyRepository.existsByCorpName(companyDTO.getCorpName()) ||
                edit && companyRepository.existsByCompanyIdIsNotAndCorpName(id, companyDTO.getCorpName())) {
            return new Result("Bunday kompaniya bor", false);
        }

        Optional<Address> optionalAddress = addressRepository.findById(companyDTO.getAddress_id());
        if (!optionalAddress.isPresent()) {
            return new Result(messageAddress.getMessage(), false);
        }
        Address address = optionalAddress.get();

        if (create && companyRepository.existsByAddress_AddressId(companyDTO.getAddress_id()) ||
                edit && companyRepository.existsByCompanyIdIsNotAndAddress_AddressId(id, companyDTO.getAddress_id())) {
            return new Result("Bu manzilda boshqa kompaniya bor", false);
        }

        company.setAddress(address);
        company.setCorpName(companyDTO.getCorpName());
        company.setDirectorName(companyDTO.getDirectorName());
        return new Result(true, company);
    }

    public Result addCompany(CompanyDTO companyDTO) {
        Result result = addingCompany(companyDTO, true, false, null);
        if (result.isSuccess()) {
            Company company = (Company) result.getObject();
            companyRepository.save(company);
            return new Result("Company saqlandi", true);
        }
        return result;
    }

    public Result editCompany(Integer id, CompanyDTO companyDTO) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            Result result = addingCompany(companyDTO, false, true, id);
            if (result.isSuccess()) {
                Company editCompany = optionalCompany.get();
                Company company = (Company) result.getObject();
                editCompany.setDirectorName(company.getDirectorName());
                editCompany.setCorpName(company.getCorpName());
                editCompany.setAddress(company.getAddress());
                companyRepository.save(editCompany);
                return new Result("Company o'zgartirildi", true);
            }
            return result;
        }
        return new Result(messageCompany.getMessage(), false);
    }

    public Result deleteCompany(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            companyRepository.delete(optionalCompany.get());
            return new Result("Company o'chirildi", true);
        }
        return new Result(messageCompany.getMessage(), false);
    }
}
