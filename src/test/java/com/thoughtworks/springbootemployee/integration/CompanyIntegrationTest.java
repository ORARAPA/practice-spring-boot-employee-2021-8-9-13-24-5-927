package com.thoughtworks.springbootemployee.integration;

import antlr.build.Tool;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeRepository employeeRepository;


    @AfterEach
    void tearDown(){
        companyRepository.deleteAll();
    }


    @Test
    void should_return_all_companies_when_getAllCompanies() throws Exception{
        //given
        companyRepository.save(new Company(1,"MIS", null));
        companyRepository.save(new Company(2,"EDI", null));
        companyRepository.save(new Company(3,"LODS", null));
        companyRepository.save(new Company(4,"IRIS", null));

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)));
    }

    @Test
    public void should_return_employees_when_call_get_employees_by_company_id_api() throws Exception{
        //given
        final Company company = new Company(1,"MIS",null);
        Company savedCompany = companyRepository.save(company);
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee(1,"Red",22,"male",1000,savedCompany.getId()));
        employeeList.add(new Employee(2,"Sharlz",23,"female",500,savedCompany.getId()));
        employeeList.add(new Employee(3,"Ken",23,"male",1000,savedCompany.getId()));
        employeeRepository.saveAll(employeeList);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/{companyId}/employees",savedCompany.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("Red"))
                .andExpect(jsonPath("$[1].name").value("Sharlz"))
                .andExpect(jsonPath("$[2].name").value("Ken"))
                .andExpect(jsonPath("$[3].name").doesNotExist());
    }


    @Test
    void should_return_company_when_findById_given_company_id() throws Exception {
        //given
        final Company company = new Company(1,"MIS",null);
        Company savedCompany = companyRepository.save(company);

        Integer id = savedCompany.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/companies/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("MIS"))
                .andExpect(jsonPath("$.employees").isEmpty());
    }

    @Test
    void should_return_two_company_per_list_when_getListByPagination_given_pageIndex_is_1_and_pageSize_is_2() throws Exception {
        //given
        companyRepository.save(new Company(1,"MIS", null));
        companyRepository.save(new Company(2,"EDI", null));
        companyRepository.save(new Company(3,"LODS", null));
        companyRepository.save(new Company(4,"IRIS", null));

        //when
        //then
        int pageIndex = 1 ,pageSize = 2;
        mockMvc.perform(MockMvcRequestBuilders.get("/companies?pageIndex={pageIndex}&pageSize={pageSize}",pageIndex,pageSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("MIS"))
                .andExpect(jsonPath("$[0].employees").isEmpty())
                .andExpect(jsonPath("$[1].companyName").value("EDI"))
                .andExpect(jsonPath("$[1].employees").isEmpty())
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    void should_create_when_addCompany_given_company_information() throws Exception {
        //given
        String employee = "{\"id\":1,\n" +
                "    \"companyName\": \"Honeybee\",\n" +
                "    \"employeesList\":[]\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyName").value("Honeybee"))
                .andExpect(jsonPath("$.employees").isEmpty());


    }

}
