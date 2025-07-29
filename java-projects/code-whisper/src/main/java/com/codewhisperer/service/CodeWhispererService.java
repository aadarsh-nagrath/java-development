package com.codewhisperer.service;

import com.codewhisperer.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CodeWhispererService {

    @Autowired
    private VoiceToTextService voiceToTextService;

    @Autowired
    private ProjectScannerService projectScannerService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private ConversationHistoryService conversationHistoryService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public CompletableFuture<CodeResponse> processVoiceRequest(VoiceRequest request) {
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        
        log.info("Processing voice request for session: {}", sessionId);

        // Step 1: Convert voice to text
        return voiceToTextService.convertVoiceToText(request.getAudioData(), sessionId)
                .thenCompose(voiceText -> {
                    // Send real-time update
                    sendWebSocketUpdate(sessionId, "Converting voice to text...", "PROCESSING");
                    
                    log.info("Voice converted to text: {}", voiceText);
                    
                    // Step 2: Scan project context
                    ProjectContext projectContext = projectScannerService.scanProject(request.getProjectPath());
                    sendWebSocketUpdate(sessionId, "Analyzing project structure...", "PROCESSING");
                    
                    // Step 3: Generate code using LLM
                    return llmService.generateCode(voiceText, projectContext, sessionId)
                            .thenApply(codeResponse -> {
                                // Step 4: Save conversation history
                                saveConversationHistory(sessionId, request.getProjectPath(), voiceText, codeResponse);
                                
                                // Step 5: Send final result
                                sendWebSocketUpdate(sessionId, "Code generation completed!", "COMPLETED");
                                
                                log.info("Code generation completed for session: {}", sessionId);
                                return codeResponse;
                            });
                })
                .exceptionally(throwable -> {
                    log.error("Error processing voice request for session: {}", sessionId, throwable);
                    sendWebSocketUpdate(sessionId, "Error: " + throwable.getMessage(), "ERROR");
                    
                    return CodeResponse.builder()
                            .generatedCode("// Error occurred during processing")
                            .explanation("An error occurred: " + throwable.getMessage())
                            .sessionId(sessionId)
                            .timestamp(System.currentTimeMillis())
                            .type(CodeResponse.ResponseType.CODE_GENERATION)
                            .build();
                });
    }

    private void sendWebSocketUpdate(String sessionId, String message, String status) {
        try {
            WebSocketUpdate update = WebSocketUpdate.builder()
                    .sessionId(sessionId)
                    .message(message)
                    .status(status)
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            messagingTemplate.convertAndSend("/topic/session/" + sessionId, update);
        } catch (Exception e) {
            log.warn("Failed to send WebSocket update for session: {}", sessionId, e);
        }
    }

    private void saveConversationHistory(String sessionId, String projectPath, String voiceInput, CodeResponse codeResponse) {
        try {
            // Check if conversation exists, if not create it
            if (!conversationHistoryService.getConversationBySessionId(sessionId).isPresent()) {
                conversationHistoryService.saveConversation(sessionId, projectPath, "default-user");
            }
            
            // Add the conversation entry
            conversationHistoryService.addConversationEntry(sessionId, voiceInput, codeResponse);
        } catch (Exception e) {
            log.warn("Failed to save conversation history for session: {}", sessionId, e);
        }
    }

    public ProjectContext getProjectContext(String projectPath) {
        return projectScannerService.scanProject(projectPath);
    }

    public ConversationHistory getConversationHistory(String sessionId) {
        return conversationHistoryService.getConversationBySessionId(sessionId)
                .orElse(null);
    }

    public boolean isServiceConfigured() {
        boolean voiceConfigured = voiceToTextService.isVoiceProviderConfigured();
        boolean llmConfigured = llmService.isLLMConfigured();
        
        log.info("Service configuration - Voice: {}, LLM: {}", voiceConfigured, llmConfigured);
        return voiceConfigured && llmConfigured;
    }

    // WebSocket update model
    public static class WebSocketUpdate {
        private String sessionId;
        private String message;
        private String status;
        private long timestamp;

        // Builder pattern
        public static WebSocketUpdateBuilder builder() {
            return new WebSocketUpdateBuilder();
        }

        public static class WebSocketUpdateBuilder {
            private WebSocketUpdate update = new WebSocketUpdate();

            public WebSocketUpdateBuilder sessionId(String sessionId) {
                update.sessionId = sessionId;
                return this;
            }

            public WebSocketUpdateBuilder message(String message) {
                update.message = message;
                return this;
            }

            public WebSocketUpdateBuilder status(String status) {
                update.status = status;
                return this;
            }

            public WebSocketUpdateBuilder timestamp(long timestamp) {
                update.timestamp = timestamp;
                return this;
            }

            public WebSocketUpdate build() {
                return update;
            }
        }

        // Getters
        public String getSessionId() { return sessionId; }
        public String getMessage() { return message; }
        public String getStatus() { return status; }
        public long getTimestamp() { return timestamp; }
    }
} 