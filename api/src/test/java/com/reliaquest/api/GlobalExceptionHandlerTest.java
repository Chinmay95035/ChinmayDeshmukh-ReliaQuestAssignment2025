package com.reliaquest.api;

import com.reliaquest.api.exception.EmployeeNotCreatedException;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.controller.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test handleEmployeeNotFoundException")
    void testHandleEmployeeNotFoundException() {
        // Arrange
        String errorMessage = "Employee with ID 1 not found";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleEmployeeNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee Not Found", response.getBody().get("Error"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    @DisplayName("Test handleEmployeeNotCreatedException")
    void testHandleEmployeeNotCreatedException() {
        // Arrange
        String errorMessage = "Failed to create employee";
        EmployeeNotCreatedException exception = new EmployeeNotCreatedException(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleEmployeeNotCreatedException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Employee Not Created", response.getBody().get("Error"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }

    @Test
    @DisplayName("Test handleGenericException")
    void testHandleGenericException() {
        // Arrange
        String errorMessage = "An unexpected error occurred";
        Exception exception = new Exception(errorMessage);

        // Act
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals(errorMessage, response.getBody().get("message"));
    }
}