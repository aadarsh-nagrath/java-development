package com.enterpriseshop.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Health Check Controller for API Gateway
 * 
 * Provides:
 * - Gateway health status
 * - Service discovery information
 * - Circuit breaker status
 * - Rate limiter status
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Autowired
    private DiscoveryClient discoveryClient;
    
    /**
     * Basic health check
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "api-gateway");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Detailed health check with service discovery
     */
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "api-gateway");
        response.put("version", "1.0.0");
        
        // Service discovery information
        Map<String, Object> discovery = new HashMap<>();
        List<String> services = discoveryClient.getServices();
        discovery.put("totalServices", services.size());
        discovery.put("services", services);
        
        // Service instances
        Map<String, Integer> serviceInstances = new HashMap<>();
        for (String service : services) {
            int instances = discoveryClient.getInstances(service).size();
            serviceInstances.put(service, instances);
        }
        discovery.put("instances", serviceInstances);
        
        response.put("discovery", discovery);
        
        // Gateway status
        Map<String, Object> gateway = new HashMap<>();
        gateway.put("routing", "ENABLED");
        gateway.put("rateLimiting", "ENABLED");
        gateway.put("circuitBreaker", "ENABLED");
        gateway.put("security", "ENABLED");
        
        response.put("gateway", gateway);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Service-specific health check
     */
    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> servicesHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        
        List<String> services = discoveryClient.getServices();
        Map<String, Object> serviceStatus = new HashMap<>();
        
        for (String service : services) {
            Map<String, Object> serviceInfo = new HashMap<>();
            List<org.springframework.cloud.client.ServiceInstance> instances = discoveryClient.getInstances(service);
            
            serviceInfo.put("instances", instances.size());
            serviceInfo.put("status", instances.isEmpty() ? "DOWN" : "UP");
            
            if (!instances.isEmpty()) {
                List<String> uris = instances.stream()
                        .map(instance -> instance.getUri().toString())
                        .collect(Collectors.toList());
                serviceInfo.put("uris", uris);
            }
            
            serviceStatus.put(service, serviceInfo);
        }
        
        response.put("services", serviceStatus);
        response.put("totalServices", services.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Circuit breaker status
     */
    @GetMapping("/circuit-breakers")
    public ResponseEntity<Map<String, Object>> circuitBreakerStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> circuitBreakers = new HashMap<>();
        
        // Auth service circuit breaker
        Map<String, Object> authCircuitBreaker = new HashMap<>();
        authCircuitBreaker.put("name", "auth-service-circuit-breaker");
        authCircuitBreaker.put("status", "CLOSED"); // Default status
        authCircuitBreaker.put("failureThreshold", 50);
        authCircuitBreaker.put("waitDuration", "30s");
        
        // User service circuit breaker
        Map<String, Object> userCircuitBreaker = new HashMap<>();
        userCircuitBreaker.put("name", "user-service-circuit-breaker");
        userCircuitBreaker.put("status", "CLOSED"); // Default status
        userCircuitBreaker.put("failureThreshold", 50);
        userCircuitBreaker.put("waitDuration", "30s");
        
        circuitBreakers.put("auth-service", authCircuitBreaker);
        circuitBreakers.put("user-service", userCircuitBreaker);
        
        response.put("circuitBreakers", circuitBreakers);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Rate limiter status
     */
    @GetMapping("/rate-limiters")
    public ResponseEntity<Map<String, Object>> rateLimiterStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> rateLimiters = new HashMap<>();
        
        // Auth service rate limiter
        Map<String, Object> authRateLimiter = new HashMap<>();
        authRateLimiter.put("service", "auth-service");
        authRateLimiter.put("replenishRate", 5);
        authRateLimiter.put("burstCapacity", 10);
        authRateLimiter.put("description", "5 requests per second, burst of 10");
        
        // User service rate limiter
        Map<String, Object> userRateLimiter = new HashMap<>();
        userRateLimiter.put("service", "user-service");
        userRateLimiter.put("replenishRate", 20);
        userRateLimiter.put("burstCapacity", 40);
        userRateLimiter.put("description", "20 requests per second, burst of 40");
        
        // Default rate limiter
        Map<String, Object> defaultRateLimiter = new HashMap<>();
        defaultRateLimiter.put("service", "default");
        defaultRateLimiter.put("replenishRate", 10);
        defaultRateLimiter.put("burstCapacity", 20);
        defaultRateLimiter.put("description", "10 requests per second, burst of 20");
        
        rateLimiters.put("auth-service", authRateLimiter);
        rateLimiters.put("user-service", userRateLimiter);
        rateLimiters.put("default", defaultRateLimiter);
        
        response.put("rateLimiters", rateLimiters);
        
        return ResponseEntity.ok(response);
    }
}
