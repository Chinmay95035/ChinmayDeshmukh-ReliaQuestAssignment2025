package com.reliaquest.api.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class Employee {


    private String id;

    @NotBlank(message = "Employee name cannot be null or empty")
    private String name;

    @Min(value = 0, message = "Salary must be non-negative")
    private int salary;

    @Min(value = 18, message = "Employee must be at least 18 years old")
    @Max(value = 65, message = "Employee age cannot exceed 65 years")
    private int age;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be null or empty")
    private String email;

    public Employee(String id, String name, int salary, int age, String title, String email) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.title = title;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    // Getters and Setters...
}
