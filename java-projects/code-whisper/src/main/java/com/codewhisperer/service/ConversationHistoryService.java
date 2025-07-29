package com.codewhisperer.service;

import com.codewhisperer.model.ConversationHistory;
import com.codewhisperer.model.CodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Slf4j
@Service
public class ConversationHistoryService {

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    // In-memory fallback storage when MongoDB is not available
    private final Map<String, ConversationHistory> inMemoryStorage = new ConcurrentHashMap<>();

    public ConversationHistory saveConversation(String sessionId, String projectPath, String userId) {
        ConversationHistory conversation = ConversationHistory.builder()
                .sessionId(sessionId)
                .projectPath(projectPath)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .entries(new ArrayList<>())
                .build();

        if (mongoTemplate != null) {
            try {
                return mongoTemplate.save(conversation);
            } catch (Exception e) {
                log.warn("MongoDB not available, using in-memory storage for session: {}", sessionId, e);
                inMemoryStorage.put(sessionId, conversation);
                return conversation;
            }
        } else {
            log.info("MongoDB not configured, using in-memory storage for session: {}", sessionId);
            inMemoryStorage.put(sessionId, conversation);
            return conversation;
        }
    }

    public void addConversationEntry(String sessionId, String voiceInput, CodeResponse codeResponse) {
        ConversationHistory conversation = null;
        
        if (mongoTemplate != null) {
            try {
                Query query = new Query(Criteria.where("sessionId").is(sessionId));
                conversation = mongoTemplate.findOne(query, ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, using in-memory storage for session: {}", sessionId, e);
                conversation = inMemoryStorage.get(sessionId);
            }
        } else {
            conversation = inMemoryStorage.get(sessionId);
        }

        if (conversation != null) {
            ConversationHistory.ConversationEntry entry = ConversationHistory.ConversationEntry.builder()
                    .voiceInput(voiceInput)
                    .generatedCode(codeResponse.getGeneratedCode())
                    .explanation(codeResponse.getExplanation())
                    .timestamp(LocalDateTime.now())
                    .type(codeResponse.getType())
                    .build();

            conversation.getEntries().add(entry);
            
            if (mongoTemplate != null) {
                try {
                    mongoTemplate.save(conversation);
                } catch (Exception e) {
                    log.warn("Failed to save to MongoDB, updating in-memory storage for session: {}", sessionId, e);
                    inMemoryStorage.put(sessionId, conversation);
                }
            } else {
                inMemoryStorage.put(sessionId, conversation);
            }
            
            log.info("Added conversation entry for session: {}", sessionId);
        } else {
            log.warn("Conversation not found for session: {}", sessionId);
        }
    }

    public Optional<ConversationHistory> getConversationBySessionId(String sessionId) {
        ConversationHistory conversation = null;
        
        if (mongoTemplate != null) {
            try {
                Query query = new Query(Criteria.where("sessionId").is(sessionId));
                conversation = mongoTemplate.findOne(query, ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, checking in-memory storage for session: {}", sessionId, e);
                conversation = inMemoryStorage.get(sessionId);
            }
        } else {
            conversation = inMemoryStorage.get(sessionId);
        }
        
        return Optional.ofNullable(conversation);
    }

    public List<ConversationHistory> getConversationsByProjectPath(String projectPath) {
        if (mongoTemplate != null) {
            try {
                Query query = new Query(Criteria.where("projectPath").is(projectPath));
                return mongoTemplate.find(query, ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, returning empty list for project path: {}", projectPath, e);
                return new ArrayList<>();
            }
        } else {
            return inMemoryStorage.values().stream()
                    .filter(conv -> projectPath.equals(conv.getProjectPath()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    public List<ConversationHistory> getConversationsByUserId(String userId) {
        if (mongoTemplate != null) {
            try {
                Query query = new Query(Criteria.where("userId").is(userId));
                return mongoTemplate.find(query, ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, returning empty list for user: {}", userId, e);
                return new ArrayList<>();
            }
        } else {
            return inMemoryStorage.values().stream()
                    .filter(conv -> userId.equals(conv.getUserId()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    public List<ConversationHistory> getRecentConversations(int limit) {
        if (mongoTemplate != null) {
            try {
                Query query = new Query();
                query.limit(limit);
                query.with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
                return mongoTemplate.find(query, ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, returning in-memory recent conversations", e);
                return inMemoryStorage.values().stream()
                        .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                        .limit(limit)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            }
        } else {
            return inMemoryStorage.values().stream()
                    .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                    .limit(limit)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    public void deleteConversation(String sessionId) {
        if (mongoTemplate != null) {
            try {
                Query query = new Query(Criteria.where("sessionId").is(sessionId));
                mongoTemplate.remove(query, ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, removing from in-memory storage for session: {}", sessionId, e);
                inMemoryStorage.remove(sessionId);
            }
        } else {
            inMemoryStorage.remove(sessionId);
        }
        log.info("Deleted conversation for session: {}", sessionId);
    }

    public long getConversationCount() {
        if (mongoTemplate != null) {
            try {
                return mongoTemplate.count(new Query(), ConversationHistory.class);
            } catch (Exception e) {
                log.warn("MongoDB not available, returning in-memory count", e);
                return inMemoryStorage.size();
            }
        } else {
            return inMemoryStorage.size();
        }
    }
} 