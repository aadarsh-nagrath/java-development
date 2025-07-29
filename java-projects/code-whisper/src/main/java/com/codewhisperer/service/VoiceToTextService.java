package com.codewhisperer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class VoiceToTextService {

    @Value("${codewhisperer.openai.api-key:}")
    private String openaiApiKey;

    @Value("${codewhisperer.voice.provider:mock}")
    private String voiceProvider;

    // Mock responses for development - replace with real API calls
    private static final Map<String, String> MOCK_RESPONSES = new HashMap<>();
    
    static {
        MOCK_RESPONSES.put("generate_controller", "generate a Spring Boot REST controller for user management");
        MOCK_RESPONSES.put("explain_error", "explain this error in my code");
        MOCK_RESPONSES.put("create_service", "create a service class that connects to Redis");
        MOCK_RESPONSES.put("add_endpoint", "add a new endpoint to update user information");
        MOCK_RESPONSES.put("fix_bug", "fix the bug in the authentication service");
    }

    public CompletableFuture<String> convertVoiceToText(String audioData, String sessionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Converting voice to text for session: {}", sessionId);
                
                if ("mock".equals(voiceProvider)) {
                    return processMockVoiceInput(audioData, sessionId);
                } else {
                    return processRealVoiceInput(audioData);
                }
                
            } catch (Exception e) {
                log.error("Error converting voice to text for session: {}", sessionId, e);
                throw new RuntimeException("Failed to convert voice to text", e);
            }
        });
    }

    private String processMockVoiceInput(String audioData, String sessionId) {
        // Simulate processing time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Decode base64 audio data (in real implementation, this would be sent to Whisper API)
        byte[] decodedAudio = Base64.getDecoder().decode(audioData);
        log.info("Processed audio data of size: {} bytes", decodedAudio.length);

        // Return a mock response based on session or random selection
        String[] mockCommands = MOCK_RESPONSES.values().toArray(new String[0]);
        int index = Math.abs(sessionId.hashCode()) % mockCommands.length;
        String mockResponse = mockCommands[index];
        
        log.info("Mock voice-to-text result: {}", mockResponse);
        return mockResponse;
    }

    private String processRealVoiceInput(String audioData) {
        // TODO: Implement real Whisper API integration
        // This would involve:
        // 1. Decoding the base64 audio data
        // 2. Sending to OpenAI Whisper API
        // 3. Processing the response
        
        if (openaiApiKey == null || openaiApiKey.isEmpty()) {
            throw new RuntimeException("OpenAI API key not configured");
        }

        // Placeholder for real implementation
        log.warn("Real voice-to-text not implemented yet");
        return "Real voice-to-text processing not implemented";
    }

    public boolean isVoiceProviderConfigured() {
        return !"mock".equals(voiceProvider) && openaiApiKey != null && !openaiApiKey.isEmpty();
    }
} 