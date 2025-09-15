package com.reliaquest.api.controller;

import com.reliaquest.api.exception.EmployeeNotCreatedException;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildErrorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("Error", error);
        errorResponse.put("Message", message);
        errorResponse.put("Status", status.value());
        errorResponse.put("Timestamp", java.time.LocalDateTime.now());
        return errorResponse;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return new ResponseEntity<>(buildErrorResponse("Employee Not Found", ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeNotCreatedException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotCreatedException(EmployeeNotCreatedException ex) {
        return new ResponseEntity<>(buildErrorResponse("Employee Not Created", ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(buildErrorResponse("Internal Server Error", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(buildErrorResponse("Bad Request", ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        return new ResponseEntity<>(buildErrorResponse("Validation Failed", fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

}
