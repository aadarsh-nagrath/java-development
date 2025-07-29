package com.codewhisperer.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "conversations")
public class ConversationHistory {
    @Id
    private String id;
    private String sessionId;
    private String projectPath;
    private String userId;
    private LocalDateTime createdAt;
    private List<ConversationEntry> entries;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationEntry {
        private String voiceInput;
        private String generatedCode;
        private String explanation;
        private LocalDateTime timestamp;
        private CodeResponse.ResponseType type;
    }
} 