package com.codewhisperer.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceRequest {
    private String audioData; // Base64 encoded audio
    private String projectPath; // Path to the current project
    private String language; // Programming language context
    private String sessionId; // For tracking conversation
} 