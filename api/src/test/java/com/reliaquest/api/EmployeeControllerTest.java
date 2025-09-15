
package com.reliaquest.api;
import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getAllEmployees - Success")
    void testGetAllEmployees() {
        // Arrange
        List<Employee> mockEmployees = List.of(
            new Employee("1", "John Doe", 50000, 30, "Developer", "john.doe@example.com"),
            new Employee("2", "Jane Smith", 60000, 28, "Manager", "jane.smith@example.com")
        );
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        // Act
        ResponseEntity<List<Object>> response = employeeController.getAllEmployees();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("Test getEmployeeById - Success")
    void testGetEmployeeById() {
        // Arrange
        String id = "1";
        Employee mockEmployee = new Employee("1", "John Doe", 50000, 30, "Developer", "john.doe@example.com");
        when(employeeService.getEmployeeById(id)).thenReturn(mockEmployee);

        // Act
        ResponseEntity<Object> response = employeeController.getEmployeeById(id);

        // Assert
        assertNotNull(response);
        assertEquals("John Doe", ((Employee) response.getBody()).getName());
        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @Test
    @DisplayName("Test getHighestSalaryOfEmployees - Success")
    void testGetHighestSalaryOfEmployees() {
        // Arrange
        int highestSalary = 60000;
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        // Act
        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        // Assert
        assertNotNull(response);
        assertEquals(highestSalary, response.getBody());
        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    @DisplayName("Test createEmployee - Success")
    void testCreateEmployee() {
        // Arrange
        Employee newEmployee = new Employee("3", "Alice Brown", 70000, 35, "Director", "alice.brown@example.com");
        when(employeeService.createEmployee(newEmployee)).thenReturn(newEmployee);

        // Act
        ResponseEntity<Object> response = employeeController.createEmployee(newEmployee);

        // Assert
        assertNotNull(response);
        assertEquals("Alice Brown", ((Employee) response.getBody()).getName());
        verify(employeeService, times(1)).createEmployee(newEmployee);
    }

    @Test
    @DisplayName("Test deleteEmployeeById - Success")
    void testDeleteEmployeeById() {
        // Arrange
        String id = "1";
        String expectedMessage = "Employee with ID 1 deleted successfully.";
        when(employeeService.deleteEmployeeById(id)).thenReturn(expectedMessage);

        // Act
        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);

        // Assert
        assertNotNull(response);
        assertEquals(expectedMessage, response.getBody());
        verify(employeeService, times(1)).deleteEmployeeById(id);
    }
}