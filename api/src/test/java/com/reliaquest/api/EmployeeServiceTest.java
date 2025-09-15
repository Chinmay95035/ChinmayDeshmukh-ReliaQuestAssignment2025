package com.reliaquest.api;


import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeService employeeService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        employeeService = new EmployeeService();
        employeeService.restTemplate = restTemplate;
    }

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";

 @Test
 @DisplayName("Test getAllEmployees - Success with Mocked External Service")
 void testGetAllEmployees_MockedExternalService() {
     // Arrange
     String url = "http://localhost:8112/api/v1/employee";
     Map<String, Object> mockResponse = Map.of(
         "data", List.of(
             Map.of(
                 "id", "1",
                 "employee_name", "John Doe",
                 "employee_salary", 50000,
                 "employee_age", 30,
                 "employee_title", "Developer",
                 "employee_email", "john.doe@example.com"
             ),
             Map.of(
                 "id", "2",
                 "employee_name", "Jane Smith",
                 "employee_salary", 60000,
                 "employee_age", 28,
                 "employee_title", "Manager",
                 "employee_email", "jane.smith@example.com"
             )
         )
     );
     when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

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
        // Arrange
        String id = "123";
        when(restTemplate.getForObject(anyString(), eq(Employee.class))).thenReturn(null);

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(id));
    }

    @Test
    @DisplayName("Test getEmployeeById - Success")
    void testGetEmployeeById_Success() {
        // Arrange
        String id = "1";
        Map<String, Object> mockResponse = Map.of(
            "id", "1",
            "employee_name", "John Doe",
            "employee_salary", 50000,
            "employee_age", 30,
            "employee_title", "Developer",
            "employee_email", "john.doe@example.com"
        );
        when(restTemplate.getForObject(BASE_URL + "/" + id, Map.class)).thenReturn(mockResponse);

        // Act
        Employee employee = employeeService.getEmployeeById(id);

        // Assert
        assertNotNull(employee);
        assertEquals("John Doe", employee.getName());
        assertEquals(50000, employee.getSalary());
    }

   @Test
@DisplayName("Test getEmployeesByNameSearch - Mock External Call")
void testGetEmployeesByNameSearch_MockExternalCall() {
    // Arrange
    String name = "John";
    String url = "http://localhost:8112/api/v1/employee";
    Map<String, Object> mockResponse = Map.of(
        "data", List.of(
            Map.of(
                "id", "1",
                "employee_name", "John Doe",
                "employee_salary", 50000,
                "employee_age", 30,
                "employee_title", "Developer",
                "employee_email", "john.doe@example.com"
            ),
            Map.of(
                "id", "2",
                "employee_name", "Jane Smith",
                "employee_salary", 60000,
                "employee_age", 28,
                "employee_title", "Manager",
                "employee_email", "jane.smith@example.com"
            )
        )
    );
    when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

    // Act
    List<Object> employees = employeeService.getEmployeesByNameSearch(name);

    // Assert
    assertNotNull(employees);
    assertEquals(1, employees.size());
    assertEquals("John Doe", ((Employee) employees.get(0)).getName());
}
   @Test
    @DisplayName("Test getHighestSalaryOfEmployees - Mock External Call")
    void testGetHighestSalaryOfEmployees_MockExternalCall() {
        // Arrange
        String url = "http://localhost:8112/api/v1/employee";
        Map<String, Object> mockResponse = Map.of(
            "data", List.of(
                Map.of(
                    "id", "1",
                    "employee_name", "John Doe",
                    "employee_salary", 50000,
                    "employee_age", 30,
                    "employee_title", "Developer",
                    "employee_email", "john.doe@example.com"
                ),
                Map.of(
                    "id", "2",
                    "employee_name", "Jane Smith",
                    "employee_salary", 60000,
                    "employee_age", 28,
                    "employee_title", "Manager",
                    "employee_email", "jane.smith@example.com"
                )
            )
        );
        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // Act
        int highestSalary = employeeService.getHighestSalaryOfEmployees();

        // Assert
        assertEquals(60000, highestSalary);
    }

    @Test
    @DisplayName("Test getTop10HighestEarningEmployeeNames - Mock External Call")
    void testGetTop10HighestEarningEmployeeNames_MockExternalCall() {
        // Arrange
        String url = "http://localhost:8112/api/v1/employee";
        Map<String, Object> mockResponse = Map.of(
            "data", List.of(
                Map.of(
                    "id", "1",
                    "employee_name", "John Doe",
                    "employee_salary", 50000,
                    "employee_age", 30,
                    "employee_title", "Developer",
                    "employee_email", "john.doe@example.com"
                ),
                Map.of(
                    "id", "2",
                    "employee_name", "Jane Smith",
                    "employee_salary", 60000,
                    "employee_age", 28,
                    "employee_title", "Manager",
                    "employee_email", "jane.smith@example.com"
                ),
                Map.of(
                    "id", "3",
                    "employee_name", "Alice Brown",
                    "employee_salary", 70000,
                    "employee_age", 35,
                    "employee_title", "Director",
                    "employee_email", "alice.brown@example.com"
                )
            )
        );
        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // Act
        List<String> topEmployees = employeeService.getTop10HighestEarningEmployeeNames();

        // Assert
        assertNotNull(topEmployees);
        assertEquals(3, topEmployees.size());
        assertEquals("Alice Brown", topEmployees.get(0));
    }

    @Test
    @DisplayName("Test createEmployee - Success")
    void testCreateEmployee_Success() {
        // Arrange
        Employee newEmployee = new Employee("3", "Alice Brown", 70000, 35, "Director", "alice.brown@example.com");
        Map<String, Object> mockResponse = Map.of(
            "id", "3",
            "employee_name", "Alice Brown",
            "employee_salary", 70000,
            "employee_age", 35,
            "employee_title", "Director",
            "employee_email", "alice.brown@example.com"
        );
        when(restTemplate.postForObject(BASE_URL, newEmployee, Map.class)).thenReturn(mockResponse);

        // Act
        Employee createdEmployee = employeeService.createEmployee(newEmployee);

        // Assert
        assertNotNull(createdEmployee);
        assertEquals("Alice Brown", createdEmployee.getName());
        assertEquals(70000, createdEmployee.getSalary());
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Success")
    void testDeleteEmployeeById_Success() {
        // Arrange
        String id = "1";

        // Act
        String result = employeeService.deleteEmployeeById(id);

        // Assert
        assertEquals("Employee with ID 1 deleted successfully.", result);
        verify(restTemplate, times(1)).delete(BASE_URL + "/" + id);
    }



    @AfterEach
    void tearDown() {
        // Cleanup resources if needed
    }
}