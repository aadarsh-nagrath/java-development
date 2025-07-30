package com.example.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class
 * This is the entry point for our CRUD application
 * 
 * Features demonstrated:
 * - RESTful APIs using Spring Boot
 * - DTOs for clean data transfer
 * - Jackson for JSON processing (default with Spring Boot)
 * - Validation using javax.validation
 */
@SpringBootApplication
public class CrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }
} 