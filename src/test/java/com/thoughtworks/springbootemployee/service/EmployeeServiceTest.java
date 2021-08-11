package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private List<Employee> testEmployees;

    @BeforeEach
    public void setup(){
        testEmployees = Arrays.asList
                (new Employee(1, "Lara", 21, "female", 1000),
                        (new Employee(2,"Cedie",21,"male",500)),
                        (new Employee(3,"Kitz",22,"male",1000)),
                        (new Employee(4,"Robert",23,"male",500)),
                        (new Employee(5,"Kyle",24,"male",1000))
                );
    }

    @Test
    void should_return_all_employees_when_getAllEmployees_given_all_employees(){
        //given
        given(employeeRepository.getEmployees()).willReturn(testEmployees);

        //when
        List<Employee> actualEmployees = employeeService.getAllEmployees();

        //then
        assertEquals(testEmployees.size(), actualEmployees.size());
        assertIterableEquals(testEmployees, actualEmployees);
    }

    @Test
    void should_return_specific_employee_when_findById_given_employee_id() {
        //given
        given(employeeRepository.getEmployees()).willReturn(testEmployees);

        //when
        Employee mockEmployee = new Employee(2, "Cedie", 23, "Male", 5000);
        Employee actualEmployee = employeeService.findById(1);

        //then
        assertEquals(mockEmployee.getEmployeeId(), actualEmployee.getEmployeeId());
    }

    @Test
    void should_return_specific_employee_when_findByGender_given_employee_gender() {
        //given
        given(employeeRepository.getEmployees()).willReturn(testEmployees);
        List<Employee> mockEmployee = testEmployees;

        //when
        List<Employee> actualEmployees = employeeService.findByGender("Male");

        //then
        assertEquals(5,actualEmployees.stream().map(Employee::getEmployeeGender).filter(employeeGender -> employeeGender.equals("Male")));
    }
}
