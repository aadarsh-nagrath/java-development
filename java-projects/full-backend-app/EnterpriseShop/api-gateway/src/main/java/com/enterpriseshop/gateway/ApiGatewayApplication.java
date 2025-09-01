package com.enterpriseshop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for API Gateway
 * 
 * This service provides:
 * - Request routing to microservices
 * - Rate limiting and security
 * - Load balancing
 * - Circuit breaker patterns
 * - API documentation aggregation
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
