package com.codewhisperer.controller;

import com.codewhisperer.model.VoiceRequest;
import com.codewhisperer.model.CodeResponse;
import com.codewhisperer.model.ProjectContext;
import com.codewhisperer.model.ConversationHistory;
import com.codewhisperer.service.CodeWhispererService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/voice")
@CrossOrigin(origins = "*")
public class VoiceController {

    @Autowired
    private CodeWhispererService codeWhispererService;

    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<CodeResponse>> processVoiceRequest(@RequestBody VoiceRequest request) {
        log.info("Received voice request for project: {}", request.getProjectPath());
        
        try {
            if (codeWhispererService == null) {
                log.error("CodeWhispererService is not injected");
                return CompletableFuture.completedFuture(
                    ResponseEntity.internalServerError()
                        .body(CodeResponse.builder()
                            .generatedCode("// Service not available")
                            .explanation("CodeWhispererService is not available")
                            .sessionId(request.getSessionId())
                            .timestamp(System.currentTimeMillis())
                            .type(CodeResponse.ResponseType.CODE_GENERATION)
                            .build())
                );
            }
            
            return codeWhispererService.processVoiceRequest(request)
                    .thenApply(response -> {
                        log.info("Voice request processed successfully for session: {}", response.getSessionId());
                        return ResponseEntity.ok(response);
                    })
                    .exceptionally(throwable -> {
                        log.error("Error processing voice request", throwable);
                        return ResponseEntity.internalServerError()
                                .body(CodeResponse.builder()
                                        .generatedCode("// Error occurred")
                                        .explanation("Failed to process voice request: " + throwable.getMessage())
                                        .sessionId(request.getSessionId())
                                        .timestamp(System.currentTimeMillis())
                                        .type(CodeResponse.ResponseType.CODE_GENERATION)
                                        .build());
                    });
        } catch (Exception e) {
            log.error("Unexpected error in processVoiceRequest", e);
            return CompletableFuture.completedFuture(
                ResponseEntity.internalServerError()
                    .body(CodeResponse.builder()
                        .generatedCode("// Unexpected error")
                        .explanation("Unexpected error: " + e.getMessage())
                        .sessionId(request.getSessionId())
                        .timestamp(System.currentTimeMillis())
                        .type(CodeResponse.ResponseType.CODE_GENERATION)
                        .build())
            );
        }
    }

    @PostMapping("/process-simple")
    public ResponseEntity<CodeResponse> processVoiceRequestSimple(@RequestBody VoiceRequest request) {
        log.info("Received simple voice request for project: {}", request.getProjectPath());
        
        try {
            if (codeWhispererService == null) {
                log.error("CodeWhispererService is not injected");
                return ResponseEntity.internalServerError()
                    .body(CodeResponse.builder()
                        .generatedCode("// Service not available")
                        .explanation("CodeWhispererService is not available")
                        .sessionId(request.getSessionId())
                        .timestamp(System.currentTimeMillis())
                        .type(CodeResponse.ResponseType.CODE_GENERATION)
                        .build());
            }
            
            // Create a simple mock response for now
            CodeResponse mockResponse = CodeResponse.builder()
                .generatedCode("// Generated code from voice request")
                .explanation("This is a mock response from the voice processing service")
                .filePath("src/main/java/com/example/VoiceController.java")
                .language("java")
                .sessionId(request.getSessionId())
                .timestamp(System.currentTimeMillis())
                .type(CodeResponse.ResponseType.CODE_GENERATION)
                .build();
            
            log.info("Simple voice request processed successfully for session: {}", request.getSessionId());
            return ResponseEntity.ok(mockResponse);
            
        } catch (Exception e) {
            log.error("Unexpected error in processVoiceRequestSimple", e);
            return ResponseEntity.internalServerError()
                .body(CodeResponse.builder()
                    .generatedCode("// Unexpected error")
                    .explanation("Unexpected error: " + e.getMessage())
                    .sessionId(request.getSessionId())
                    .timestamp(System.currentTimeMillis())
                    .type(CodeResponse.ResponseType.CODE_GENERATION)
                    .build());
        }
    }

    @PostMapping("/test-process")
    public ResponseEntity<CodeResponse> testProcessVoiceRequest(@RequestBody VoiceRequest request) {
        log.info("Test process endpoint called with project: {}", request.getProjectPath());
        
        try {
            if (codeWhispererService == null) {
                log.error("CodeWhispererService is not injected");
                return ResponseEntity.internalServerError()
                    .body(CodeResponse.builder()
                        .generatedCode("// Service not available")
                        .explanation("CodeWhispererService is not available")
                        .sessionId(request.getSessionId())
                        .timestamp(System.currentTimeMillis())
                        .type(CodeResponse.ResponseType.CODE_GENERATION)
                        .build());
            }
            
            // Create a simple mock response for testing
            CodeResponse mockResponse = CodeResponse.builder()
                .generatedCode("// Test code generation")
                .explanation("This is a test response from the voice processing service")
                .filePath("src/main/java/com/example/TestController.java")
                .language("java")
                .sessionId(request.getSessionId())
                .timestamp(System.currentTimeMillis())
                .type(CodeResponse.ResponseType.CODE_GENERATION)
                .build();
            
            log.info("Test process completed successfully");
            return ResponseEntity.ok(mockResponse);
            
        } catch (Exception e) {
            log.error("Unexpected error in testProcessVoiceRequest", e);
            return ResponseEntity.internalServerError()
                .body(CodeResponse.builder()
                    .generatedCode("// Unexpected error")
                    .explanation("Unexpected error: " + e.getMessage())
                    .sessionId(request.getSessionId())
                    .timestamp(System.currentTimeMillis())
                    .type(CodeResponse.ResponseType.CODE_GENERATION)
                    .build());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("Test endpoint called");
        return ResponseEntity.ok("Test endpoint working! CodeWhispererService: " + (codeWhispererService != null));
    }

    @GetMapping("/project/{projectPath}")
    public ResponseEntity<ProjectContext> getProjectContext(@PathVariable String projectPath) {
        try {
            ProjectContext context = codeWhispererService.getProjectContext(projectPath);
            return ResponseEntity.ok(context);
        } catch (Exception e) {
            log.error("Error getting project context for path: {}", projectPath, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<ConversationHistory> getConversationHistory(@PathVariable String sessionId) {
        ConversationHistory history = codeWhispererService.getConversationHistory(sessionId);
        if (history != null) {
            return ResponseEntity.ok(history);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/status")
    public ResponseEntity<ServiceStatus> getServiceStatus() {
        boolean configured = codeWhispererService.isServiceConfigured();
        return ResponseEntity.ok(new ServiceStatus(configured));
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("CodeWhisperer is running!");
    }

    public static class ServiceStatus {
        private boolean configured;
        private String message;

        public ServiceStatus(boolean configured) {
            this.configured = configured;
            this.message = configured ? "Service is fully configured" : "Service is running in mock mode";
        }

        public boolean isConfigured() { return configured; }
        public String getMessage() { return message; }
    }
} 