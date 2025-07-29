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
    public CompletableFuture<ResponseEntity<CodeResponse>> processVoiceRequest(@Valid @RequestBody VoiceRequest request) {
        log.info("Received voice request for project: {}", request.getProjectPath());
        
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