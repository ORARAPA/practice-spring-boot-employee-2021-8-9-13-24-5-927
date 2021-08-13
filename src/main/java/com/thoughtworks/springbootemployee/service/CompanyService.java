package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public List<Employee> getAllEmployeesByCompanyId(Integer companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        return company.getEmployees();
    }

    public Company findById(Integer companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException());
    }

    public List<Company> getCompaniesByPagination(Integer pageIndex, Integer pageSize) {
        return companyRepository.findAll(PageRequest.of(pageIndex-1,pageSize)).getContent();
    }

    public Company addCompany(Company companyInfo) {
        return companyRepository.save(companyInfo);
    }

    public Company updateCompany(Integer companyId, Company companyInfo) {
        Company company = companyRepository.findById(companyId)
                .map(oldCompany -> updateCompanyInfo(oldCompany,companyInfo))
                .get();
        return companyRepository.save(company);
    }

    private Company updateCompanyInfo(Company company, Company companyInfo) {
        if (companyInfo.getCompanyName() != null){
            company.setCompanyName(companyInfo.getCompanyName());
        }
        if (companyInfo.getEmployees() != null){
            company.setEmployees(companyInfo.getEmployees());
        }
        return company;
    }

    public Company removeCompany(Integer companyId) {
        Optional<Company> removeCompany = companyRepository.findById(companyId);
        companyRepository.deleteById(companyId);
        return removeCompany.orElse(null);
    }

}
