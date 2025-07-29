package com.codewhisperer.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeResponse {
    private String generatedCode;
    private String explanation;
    private String filePath; // Suggested file path
    private String language;
    private String sessionId;
    private long timestamp;
    private ResponseType type;
    
    public enum ResponseType {
        CODE_GENERATION,
        EXPLANATION,
        ERROR_FIX,
        API_SCAFFOLDING
    }
} 