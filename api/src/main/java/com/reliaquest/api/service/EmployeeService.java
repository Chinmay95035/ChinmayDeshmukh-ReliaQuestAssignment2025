package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeNotCreatedException;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    @Autowired
    public RestTemplate restTemplate;

    public List<Employee> getAllEmployees() {
        try {
            Map<String, Object> response = restTemplate.getForObject(BASE_URL, Map.class);
            return parseEmployeeList(response);
        } catch (Exception ex) {
            throw new EmployeeNotFoundException("Failed to fetch employees from the external service.");
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new IllegalArgumentException("Employee ID cannot be null or empty.");
            }
            log.info("Fetching employee with ID: {}", id);
            Map<String, Object> response = restTemplate.getForObject(BASE_URL + "/" + id, Map.class);
            //return parseEmployee((Map<String, Object>) response.get("data"));
            return parseEmployeee(response);
        } catch (Exception ex) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }

    public List<Object> getEmployeesByNameSearch(String name) {
        try {
            log.info("Searching employees with name containing: {}", name);
            return getAllEmployees().stream()
                    .filter(emp -> emp.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new EmployeeNotFoundException("Error occurred while searching for employees with name containing: " + name);
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        try {
            return getAllEmployees().stream()
                    .map(Employee::getSalary)
                    .max(Integer::compareTo)
                    .orElse(0);
        } catch (Exception ex) {
            throw new EmployeeNotFoundException("Error occurred while fetching the highest salary.");
        }

    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        return getAllEmployees().stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public Employee createEmployee(Employee employee) {
        try {
            log.info("Creating new employee with name: {}", employee.getName());
            Map<String, Object> response = restTemplate.postForObject(BASE_URL, employee, Map.class);
            return parseEmployeee(response);
        } catch (Exception ex) {
            throw new EmployeeNotCreatedException("Error occurred while creating the employee: " + ex.getMessage());
        }
    }


    public String deleteEmployeeById(String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new IllegalArgumentException("Employee ID cannot be null or empty.");
            }
            log.info("Deleting employee with ID: {}", id);
            restTemplate.delete(BASE_URL + "/" + id);
            return "Employee with ID " + id + " deleted successfully.";
        } catch (Exception ex) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found To delete");
        }

    }

    private List<Employee> parseEmployeeList(Map<String, Object> response) {
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
        return data.stream().map(this::parseEmployee).collect(Collectors.toList());
    }


    private Employee parseEmployee(Map<String, Object> data) {
        return new Employee(
                (String) data.get("id"),
                (String) data.get("employee_name"),
                (Integer) data.get("employee_salary"),
                (Integer) data.get("employee_age"),
                (String) data.get("employee_title"),
                (String) data.get("employee_email")
        );
    }

    private Employee parseEmployeee(Map<String, Object> data) {
        LinkedHashMap castData = (LinkedHashMap) data.get("data");
        return new Employee(
                (String) castData.get("id"),
                (String) castData.get("employee_name"),
                (Integer) castData.get("employee_salary"),
                (Integer) castData.get("employee_age"),
                (String) castData.get("employee_title"),
                (String) castData.get("employee_email")
        );
    }

}