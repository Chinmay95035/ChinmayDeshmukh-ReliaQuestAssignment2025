package com.reliaquest.api;


import com.reliaquest.api.exception.EmployeeNotCreatedException;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeApiClient;
import com.reliaquest.api.service.EmployeeserviceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

    @Mock
    private EmployeeApiClient employeeApiClient; // Mocked external service client
    @InjectMocks
    EmployeeserviceImpl employeeService;

    private static final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private RestTemplate restTemplate;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Test getAllEmployees - Success with Mocked External Service")
    void testGetAllEmployees_MockedExternalService() {
        // Arrange
        Map<String, Object> mockResponse = Map.of("data", List.of(Map.of("id", "1", "employee_name", "John Doe", "employee_salary", 50000, "employee_age", 30, "employee_title", "Developer", "employee_email", "john.doe@example.com"), Map.of("id", "2", "employee_name", "Jane Smith", "employee_salary", 60000, "employee_age", 28, "employee_title", "Manager", "employee_email", "jane.smith@example.com")));

        when(employeeApiClient.callEmployeeApi(BASE_URL, null)).thenReturn(mockResponse);

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
        assertEquals("Jane Smith", employees.get(1).getName());
    }


    @Test
    @DisplayName("Test getEmployeeById - Employee Not Found")
    void testGetEmployeeById_NotFound() {
        String id = "123";
        when(employeeApiClient.callEmployeeApi(BASE_URL, id)).thenThrow(new EmployeeNotFoundException("Employee with ID " + id + " not found"));

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(id));
    }

    @Test
    @DisplayName("Test getEmployeesByNameSearch - Mock External Call")
    void testGetEmployeesByNameSearch_MockExternalCall() {
        String name = "John";
        Map<String, Object> mockResponse = Map.of("data", List.of(Map.of("id", "1", "employee_name", "John Doe", "employee_salary", 50000, "employee_age", 30, "employee_title", "Developer", "employee_email", "john.doe@example.com"), Map.of("id", "2", "employee_name", "Jane Smith", "employee_salary", 60000, "employee_age", 28, "employee_title", "Manager", "employee_email", "jane.smith@example.com")));
        when(employeeApiClient.callEmployeeApi(BASE_URL, null)).thenReturn(mockResponse);

        List<Object> employees = employeeService.getEmployeesByNameSearch(name);

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("John Doe", ((Employee) employees.get(0)).getName());
    }

    @Test
    @DisplayName("Test getHighestSalaryOfEmployees - Mock External Call")
    void testGetHighestSalaryOfEmployees_MockExternalCall() {
        Map<String, Object> mockResponse = Map.of("data", List.of(Map.of("id", "1", "employee_name", "John Doe", "employee_salary", 50000, "employee_age", 30, "employee_title", "Developer", "employee_email", "john.doe@example.com"), Map.of("id", "2", "employee_name", "Jane Smith", "employee_salary", 60000, "employee_age", 28, "employee_title", "Manager", "employee_email", "jane.smith@example.com")));
        when(employeeApiClient.callEmployeeApi(BASE_URL, null)).thenReturn(mockResponse);

        int highestSalary = employeeService.getHighestSalaryOfEmployees();

        assertEquals(60000, highestSalary);
    }

    @Test
    @DisplayName("createEmployee - External Service Unavailable")
    void testCreateEmployee_RestTemplateException() {
        Employee newEmployee = new Employee(null, "Alice Brown", 70000, 35, "Director", "alice.brown@example.com");

        when(employeeApiClient.callEmployeeApi(BASE_URL, null)).thenThrow(new RuntimeException("Employee service is temporarily unavailable:"));

        EmployeeNotCreatedException exception = assertThrows(EmployeeNotCreatedException.class, () -> employeeService.createEmployee(newEmployee));

        assertTrue(exception.getMessage().contains("Error occurred while creating the employee"));
    }

}

