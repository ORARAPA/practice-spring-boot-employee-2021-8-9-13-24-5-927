package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyMapper {
    public Company toEntity(CompanyRequest companyRequest){
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);

        return company;
    }

    public CompanyResponse toResponse(Company company){
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        companyResponse.setEmployeeNumber(company.getEmployees().size());
        return companyResponse;
    }

    public List<CompanyResponse> toResponse(List<Company> companies){
        List<CompanyResponse> companyResponses = new ArrayList<>();
        companies.forEach(currentCompany -> {
            CompanyResponse companyResponse = new CompanyResponse();
            BeanUtils.copyProperties(currentCompany, companyResponse);
            companyResponses.add(companyResponse);
        });
        return companyResponses;
    }
}
