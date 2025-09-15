package com.reliaquest.api.exception;

public class EmployeeNotCreatedException extends RuntimeException {
    public EmployeeNotCreatedException(String message) {
        super(message);
    }
}