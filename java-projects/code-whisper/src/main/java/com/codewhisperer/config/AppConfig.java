package com.codewhisperer.config;

import com.codewhisperer.service.LLMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AppConfig {

    @Autowired
    private LLMService llmService;

    @Bean
    public CommandLineRunner initializeServices() {
        return args -> {
            log.info("Initializing CodeWhisperer services...");
            
            // Initialize LLM service
            llmService.initialize();
            
            log.info("CodeWhisperer services initialized successfully!");
            log.info("Application is ready to process voice requests.");
        };
    }
} 