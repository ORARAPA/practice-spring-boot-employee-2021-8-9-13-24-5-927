package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
//import com.thoughtworks.springbootemployee.repository.RetiringEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
       this.employeeRepository = employeeRepository;
    }

    public Employee addEmployee(Employee employeeInfo) {
        return employeeRepository.save(employeeInfo);
    }

    public List<Employee> getAllEmployees() {
       return employeeRepository.findAll();
    }

    public Employee findById(Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> findByGender(String employeeGender) {
        return employeeRepository.findAllByGender(employeeGender);
    }

    public List<Employee> getEmployeesByAgeRange(Integer minAge, Integer maxAge) {
        return employeeRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Employee removeEmployee(Integer employeeId) {
        Optional<Employee> removeEmployee = employeeRepository.findById(employeeId);
        employeeRepository.deleteById(employeeId);
        return removeEmployee.orElseThrow(EmployeeNotFoundException::new);
    }

    public Employee updateEmployee(Integer employeeId, Employee employeeInfo) {
        Employee updateEmployee = employeeRepository.findById(employeeId)
                .map(employee -> updateEmployeeInfo(employee, employeeInfo))
                .get();
        return employeeRepository.save(updateEmployee);
    }

    private Employee updateEmployeeInfo(Employee employee, Employee employeeInfo) {
        if (employeeInfo.getName() != null){
            employee.setName(employeeInfo.getName());
        }
        if (employeeInfo.getAge() != null){
            employee.setAge(employeeInfo.getAge());
        }
        if (employeeInfo.getGender() != null){
            employee.setGender(employeeInfo.getGender());
        }
        if (employeeInfo.getSalary() != null){
            employee.setSalary(employeeInfo.getSalary());
        }
        if (employeeInfo.getCompanyId() != null){
            employee.setCompanyId(employeeInfo.getCompanyId());
        }
        return employee;
    }

    public List<Employee> getEmployeesByPagination(Integer pageIndex, Integer pageSize) {
        return employeeRepository.findAll(PageRequest.of(pageIndex-1,pageSize)).getContent();
    }

}
