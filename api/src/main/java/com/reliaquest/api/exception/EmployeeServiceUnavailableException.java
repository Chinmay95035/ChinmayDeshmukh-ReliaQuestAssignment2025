package com.reliaquest.api.exception;

public class EmployeeServiceUnavailableException extends RuntimeException {
    public EmployeeServiceUnavailableException(String message) {
        super(message);
    }
}