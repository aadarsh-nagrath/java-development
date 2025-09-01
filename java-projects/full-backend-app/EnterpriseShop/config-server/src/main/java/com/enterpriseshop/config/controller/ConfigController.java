package com.enterpriseshop.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration Controller for Config Server
 * 
 * Provides additional endpoints for:
 * - Configuration validation
 * - Environment information
 * - Configuration refresh
 * - Health checks
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    
    @Autowired
    private EnvironmentRepository environmentRepository;
    
    /**
     * Get configuration for a specific application and profile
     */
    @GetMapping("/{application}/{profile}")
    public ResponseEntity<Environment> getConfiguration(
            @PathVariable String application,
            @PathVariable String profile) {
        
        Environment env = environmentRepository.findOne(application, profile, null);
        return ResponseEntity.ok(env);
    }
    
    /**
     * Get configuration for a specific application, profile, and label
     */
    @GetMapping("/{application}/{profile}/{label}")
    public ResponseEntity<Environment> getConfigurationWithLabel(
            @PathVariable String application,
            @PathVariable String profile,
            @PathVariable String label) {
        
        Environment env = environmentRepository.findOne(application, profile, label);
        return ResponseEntity.ok(env);
    }
    
    /**
     * Get configuration properties for a specific application
     */
    @GetMapping("/{application}/properties")
    public ResponseEntity<Map<String, Object>> getApplicationProperties(
            @PathVariable String application,
            @RequestParam(defaultValue = "default") String profile,
            @RequestParam(required = false) String label) {
        
        Environment env = environmentRepository.findOne(application, profile, label);
        Map<String, Object> response = new HashMap<>();
        
        response.put("application", application);
        response.put("profile", profile);
        response.put("label", label);
        response.put("propertySources", env.getPropertySources());
        response.put("version", env.getVersion());
        response.put("state", env.getState());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all available applications
     */
    @GetMapping("/applications")
    public ResponseEntity<Map<String, Object>> getApplications() {
        Map<String, Object> response = new HashMap<>();
        
        // This would typically come from a service that tracks all applications
        // For now, we'll return a static list
        String[] applications = {"auth-service", "user-service", "api-gateway", "config-server"};
        response.put("applications", applications);
        response.put("count", applications.length);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all available profiles
     */
    @GetMapping("/profiles")
    public ResponseEntity<Map<String, Object>> getProfiles() {
        Map<String, Object> response = new HashMap<>();
        
        String[] profiles = {"default", "dev", "test", "staging", "prod"};
        response.put("profiles", profiles);
        response.put("count", profiles.length);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get configuration server information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getConfigServerInfo() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("service", "config-server");
        response.put("version", "1.0.0");
        response.put("description", "Centralized configuration management for EnterpriseShop");
        response.put("features", new String[]{
            "Git-based configuration",
            "Environment-specific configs",
            "Configuration encryption",
            "Auto-refresh support",
            "Health monitoring"
        });
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Validate configuration for an application
     */
    @PostMapping("/{application}/validate")
    public ResponseEntity<Map<String, Object>> validateConfiguration(
            @PathVariable String application,
            @RequestParam(defaultValue = "default") String profile,
            @RequestParam(required = false) String label) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Environment env = environmentRepository.findOne(application, profile, label);
            
            response.put("valid", true);
            response.put("application", application);
            response.put("profile", profile);
            response.put("label", label);
            response.put("propertySources", env.getPropertySources().size());
            response.put("message", "Configuration is valid");
            
        } catch (Exception e) {
            response.put("valid", false);
            response.put("application", application);
            response.put("profile", profile);
            response.put("label", label);
            response.put("error", e.getMessage());
            response.put("message", "Configuration validation failed");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Refresh configuration for an application
     */
    @PostMapping("/{application}/refresh")
    public ResponseEntity<Map<String, Object>> refreshConfiguration(
            @PathVariable String application,
            @RequestParam(defaultValue = "default") String profile,
            @RequestParam(required = false) String label) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Force refresh by getting configuration again
            Environment env = environmentRepository.findOne(application, profile, label);
            
            response.put("success", true);
            response.put("application", application);
            response.put("profile", profile);
            response.put("label", label);
            response.put("message", "Configuration refreshed successfully");
            response.put("timestamp", System.currentTimeMillis());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("application", application);
            response.put("profile", profile);
            response.put("label", label);
            response.put("error", e.getMessage());
            response.put("message", "Configuration refresh failed");
        }
        
        return ResponseEntity.ok(response);
    }
}
