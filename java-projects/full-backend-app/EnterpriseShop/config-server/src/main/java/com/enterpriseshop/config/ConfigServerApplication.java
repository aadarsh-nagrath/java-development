package com.enterpriseshop.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for Config Server
 * 
 * This service provides:
 * - Centralized configuration management
 * - Git-based configuration storage
 * - Configuration encryption/decryption
 * - Environment-specific configurations
 * - Configuration refresh endpoints
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
