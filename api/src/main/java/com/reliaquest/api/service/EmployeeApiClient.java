package com.reliaquest.api.service;

import com.reliaquest.api.exception.EmployeeServiceUnavailableException;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmployeeApiClient {
    //circuit breaker implementation when the external service is down or failing issue
    @CircuitBreaker(name = "employeeServiceCB", fallbackMethod = "employeeApiFallback")
    public Map<String, Object> callEmployeeApi(String url, String id) {
        url = (id != null) ? url + "/" + id : url;
        return new RestTemplate().getForObject(url, Map.class);
    }

    @CircuitBreaker(name = "employeeServiceCB", fallbackMethod = "employeeApiFallback")
    public Map<String, Object> callEmployeeCreateApi(String url, Employee employee, String Id) {
        url = (Id != null) ? url + "/" + Id : url;
        return new RestTemplate().postForObject(url, employee, Map.class);
    }

    public Map<String, Object> employeeApiFallback(String url, String id, Throwable ex) {
        throw new EmployeeServiceUnavailableException("Employee service is temporarily unavailable: " + ex.getMessage());
    }
}

