package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void tearDown(){
        employeeRepository.deleteAll();
    }

    @Test
    void should_return_all_employees_when_getAllEmployees() throws Exception {
        //given
        final Employee employee = new Employee(1,"Lara",22,"female",1000);
        employeeRepository.save(employee);

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("Lara"))
                .andExpect(jsonPath("$[0].age").value(22))
                .andExpect(jsonPath("$[0].gender").value("female"))
                .andExpect(jsonPath("$[0].salary").value(1000));
    }

    @Test
    void should_create_when_addEmployee_given_employee_information() throws Exception {
        //given
        String employee = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Bobber\",\n" +
                "    \"age\": 23,\n" +
                "    \"gender\": \"male\",\n" +
                "    \"salary\": 1000\n" +
                "}";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bobber"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.salary").value("1000"));

    }

    @Test
    void should_update_when_updateEmployee_given_employee_information() throws Exception {
        //given
        final Employee employee = new Employee(1,"Lara",20,"female",1000);
        final Employee savedEmployee = employeeRepository.save(employee);
        String newEmployeeInfo = "{\n" +
                "    \"name\": \"Angelo\",\n" +
                "    \"salary\": 10000\n" +
                "}";

        //when
        int id = savedEmployee.getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmployeeInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Angelo"))
                .andExpect(jsonPath("$.salary").value(10000));
    }

    @Test
    void should_remove_when_removeEmployee_given_employee_id() throws Exception {
        //given
        final Employee employee = new Employee(1,"Lara",20,"female",1000);
        final Employee savedEmployee = employeeRepository.save(employee);

        //when
        int id = savedEmployee.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_employee_when_findById_given_employee_id() throws Exception {
        //given
        final Employee employee = new Employee(1,"Lara",20,"female",1000);
        final Employee savedEmployee = employeeRepository.save(employee);

        Integer id = savedEmployee.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lara"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.salary").value(1000));
    }

    @Test
    void should_return_employees_when_findByGender_given_employee_gender() throws Exception {
        //given
        final Employee employee1 = new Employee(1,"Lara",20,"female",1000);
        employeeRepository.save(employee1);
        final Employee employee2 = new Employee(2,"Jerz",20,"male",1000);
        employeeRepository.save(employee2);
        final Employee employee3 = new Employee(3,"Ephree",20,"female",1000);
        employeeRepository.save(employee3);


        //when
        //then
        String gender = "female";
        mockMvc.perform(MockMvcRequestBuilders.get("/employees?gender={gender}",gender))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("Lara"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[0].gender").value("female"))
                .andExpect(jsonPath("$[0].salary").value(1000))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].name").value("Ephree"))
                .andExpect(jsonPath("$[1].age").value(20))
                .andExpect(jsonPath("$[1].gender").value("female"))
                .andExpect(jsonPath("$[1].salary").value(1000));
    }

    @Test
    void should_return_three_employee_per_list_when_getListByPagination_given_pageIndex_is_1_and_pageSize_is_3() throws Exception {
        //given
        final Employee employee1 = new Employee(1,"Lara",20,"female",1000);
        employeeRepository.save(employee1);
        final Employee employee2 = new Employee(2,"Jerz",20,"male",1000);
        employeeRepository.save(employee2);
        final Employee employee3 = new Employee(3,"Ephree",20,"female",1000);
        employeeRepository.save(employee3);


        //when
        //then
        int pageIndex = 1 ,pageSize = 2;
        mockMvc.perform(MockMvcRequestBuilders.get("/employees?pageIndex={pageIndex}&pageSize={pageSize}",pageIndex,pageSize))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("Lara"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[0].gender").value("female"))
                .andExpect(jsonPath("$[0].salary").value(1000))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].name").value("Jerz"))
                .andExpect(jsonPath("$[1].age").value(20))
                .andExpect(jsonPath("$[1].gender").value("male"))
                .andExpect(jsonPath("$[1].salary").value(1000))
                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

}
