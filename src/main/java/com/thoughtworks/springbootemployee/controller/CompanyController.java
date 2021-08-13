package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.dto.CompanyRequest;
import com.thoughtworks.springbootemployee.dto.CompanyResponse;
import com.thoughtworks.springbootemployee.entity.Employee;
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

    private List<Company> companies = new ArrayList<>();

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
    public List<Employee> getAllEmployeesByCompanyId(@PathVariable Integer companyId){
        return companyService.getAllEmployeesByCompanyId(companyId);
    }

    @GetMapping(params = {"pageIndex", "pageSize"})
    public List<Company> getCompaniesByPagination(@RequestParam Integer pageIndex, @RequestParam Integer pageSize) {
        return companyService.getCompaniesByPagination(pageIndex, pageSize);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody CompanyRequest companyInfo){
        return companyService.addCompany(companyMapper.toEntity(companyInfo));
    }

    @PutMapping(path = "/{companyId}")
    public Company updateCompany(@PathVariable Integer companyId, @RequestBody CompanyRequest companyInfo){
        return companyService.updateCompany(companyId, companyMapper.toEntity(companyInfo));
    }

    @DeleteMapping(path = "/{companyId}")
    public Company deleteCompany(@PathVariable Integer companyId){
        return companyService.removeCompany(companyId);
    }

}
