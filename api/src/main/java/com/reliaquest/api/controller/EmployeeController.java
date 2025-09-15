package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController<Object, Employee> {

    private static final String CB_NAME = "employeeServiceCB";

    private final  EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<List<Object>> getAllEmployees() {
        List<Object> employees = employeeService.getAllEmployees().stream().map(emp -> (Object) emp).collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    @Override
    @GetMapping("/search/{searchString}")
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<List<Object>> getEmployeesByNameSearch(@PathVariable String searchString) {
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
    }

    @Override
    @GetMapping("/{id}")
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Override
    @GetMapping("/highestSalary")
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTop10HighestEarningEmployeeNames());
    }

    @Override
    @PostMapping
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employeeInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeInput));

    }

    @Override
    @DeleteMapping("/{id}")
    @CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackResponse")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }


    /**
     * Common Fallback Method
     *
     */
    private <T> ResponseEntity<T> fallbackResponse(Throwable throwable) {
       String message = "Service temporarily unavailable. Please try again later.";
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body((T) message);
    }
}