package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(String id);

    List<Object> getEmployeesByNameSearch(String name);


    Integer getHighestSalaryOfEmployees();

    List<String> getTop10HighestEarningEmployeeNames();


    Object createEmployee(Employee employeeInput);


    String deleteEmployeeById(String id);
}
