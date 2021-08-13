package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.dto.EmployeeResponse;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    EmployeeMapper employeeMapper;

    public CompanyController() {

    }

    @GetMapping
    public List<CompanyResponse> getAllCompanies(){
        return companyMapper.toResponse(companyService.getAllCompanies());
    }


    @GetMapping(path = "/{companyId}")
    public CompanyResponse findById(@PathVariable Integer companyId){
        return companyMapper.toResponse(companyService.findById(companyId));
    }

    @GetMapping(path = "/{companyId}/employees")
    public List<EmployeeResponse> getAllEmployeesByCompanyId(@PathVariable Integer companyId){
        return employeeMapper.toResponse(companyService.getAllEmployeesByCompanyId(companyId));
    }

    @GetMapping(params = {"pageIndex", "pageSize"})
    public List<CompanyResponse> getCompaniesByPagination(@RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return companyMapper.toResponse(companyService.getCompaniesByPagination(pageIndex, pageSize));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse addCompany(@RequestBody CompanyRequest companyInfo){
        final Company company = companyService.addCompany(companyMapper.toEntity(companyInfo));
        return companyMapper.toResponse(company);
    }

    @PutMapping(path = "/{companyId}")
    public CompanyResponse updateCompany(@PathVariable Integer companyId, @RequestBody CompanyRequest companyInfo){
        final Company company =  companyService.updateCompany(companyId, companyMapper.toEntity(companyInfo));
        return companyMapper.toResponse(company);
    }

    @DeleteMapping(path = "/{companyId}")
    public CompanyResponse deleteCompany(@PathVariable Integer companyId){
        return companyMapper.toResponse(companyService.removeCompany(companyId));
    }

}
