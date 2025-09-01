package com.enterpriseshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for User Service
 * 
 * This service provides:
 * - User profile management
 * - Address management
 * - User preferences
 * - REST and GraphQL APIs
 * - Integration with Auth Service for authentication
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
