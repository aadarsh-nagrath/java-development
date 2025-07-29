package com.codewhisperer.service;

import com.codewhisperer.model.CodeResponse;
import com.codewhisperer.model.ProjectContext;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class LLMService {

    @Value("${codewhisperer.openai.api-key:}")
    private String openaiApiKey;

    @Value("${codewhisperer.openai.model:gpt-4}")
    private String model;

    @Value("${codewhisperer.openai.max-tokens:2000}")
    private Integer maxTokens;

    private OpenAiService openAiService;

    public void initialize() {
        if (openaiApiKey != null && !openaiApiKey.isEmpty()) {
            this.openAiService = new OpenAiService(openaiApiKey, Duration.ofSeconds(60));
            log.info("OpenAI service initialized with model: {}", model);
        } else {
            log.warn("OpenAI API key not configured, using mock responses");
        }
    }

    public CompletableFuture<CodeResponse> generateCode(String voiceInput, ProjectContext projectContext, String sessionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Generating code for input: {}", voiceInput);
                
                if (openAiService == null) {
                    return generateMockCode(voiceInput, projectContext, sessionId);
                } else {
                    return generateRealCode(voiceInput, projectContext, sessionId);
                }
                
            } catch (Exception e) {
                log.error("Error generating code for session: {}", sessionId, e);
                return CodeResponse.builder()
                        .generatedCode("// Error generating code: " + e.getMessage())
                        .explanation("Failed to generate code due to an error")
                        .sessionId(sessionId)
                        .timestamp(System.currentTimeMillis())
                        .type(CodeResponse.ResponseType.CODE_GENERATION)
                        .build();
            }
        });
    }

    private CodeResponse generateRealCode(String voiceInput, ProjectContext projectContext, String sessionId) {
        String prompt = buildContextAwarePrompt(voiceInput, projectContext);
        
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", getSystemPrompt()));
        messages.add(new ChatMessage("user", prompt));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .maxTokens(maxTokens)
                .temperature(0.3)
                .build();

        try {
            String response = openAiService.createChatCompletion(request)
                    .getChoices().get(0).getMessage().getContent();
            
            return parseLLMResponse(response, sessionId);
            
        } catch (Exception e) {
            log.error("Error calling OpenAI API", e);
            throw new RuntimeException("Failed to generate code via OpenAI", e);
        }
    }

    private CodeResponse generateMockCode(String voiceInput, ProjectContext projectContext, String sessionId) {
        // Simulate processing time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String mockCode = generateMockCodeBasedOnInput(voiceInput, projectContext);
        String explanation = generateMockExplanation(voiceInput);
        
        return CodeResponse.builder()
                .generatedCode(mockCode)
                .explanation(explanation)
                .filePath(suggestFilePath(voiceInput, projectContext))
                .language(projectContext.getPrimaryLanguage())
                .sessionId(sessionId)
                .timestamp(System.currentTimeMillis())
                .type(determineResponseType(voiceInput))
                .build();
    }

    private String buildContextAwarePrompt(String voiceInput, ProjectContext projectContext) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Project Context:\n");
        prompt.append("- Project Name: ").append(projectContext.getProjectName()).append("\n");
        prompt.append("- Primary Language: ").append(projectContext.getPrimaryLanguage()).append("\n");
        prompt.append("- Build Tool: ").append(projectContext.getDependencies().get("buildTool")).append("\n");
        prompt.append("- Git Branch: ").append(projectContext.getGitBranch()).append("\n");
        
        if (!projectContext.getSourceFiles().isEmpty()) {
            prompt.append("- Source Files: ").append(String.join(", ", projectContext.getSourceFiles().subList(0, Math.min(10, projectContext.getSourceFiles().size())))).append("\n");
        }
        
        prompt.append("\nVoice Input: ").append(voiceInput).append("\n\n");
        prompt.append("Please generate appropriate code based on the project context and voice input. ");
        prompt.append("Return the response in the following JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"code\": \"the generated code\",\n");
        prompt.append("  \"explanation\": \"brief explanation of what the code does\",\n");
        prompt.append("  \"filePath\": \"suggested file path\",\n");
        prompt.append("  \"type\": \"CODE_GENERATION|EXPLANATION|ERROR_FIX|API_SCAFFOLDING\"\n");
        prompt.append("}");
        
        return prompt.toString();
    }

    private String getSystemPrompt() {
        return "You are CodeWhisperer, an AI assistant that helps developers generate code based on voice commands. " +
               "You understand project context and generate appropriate, production-ready code. " +
               "Always respond with valid JSON in the specified format. " +
               "Focus on generating clean, well-documented code that follows best practices.";
    }

    private CodeResponse parseLLMResponse(String response, String sessionId) {
        try {
            // Simple JSON parsing - in production, use proper JSON library
            String code = extractJsonField(response, "code");
            String explanation = extractJsonField(response, "explanation");
            String filePath = extractJsonField(response, "filePath");
            String typeStr = extractJsonField(response, "type");
            
            CodeResponse.ResponseType type = CodeResponse.ResponseType.CODE_GENERATION;
            try {
                type = CodeResponse.ResponseType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid response type: {}", typeStr);
            }
            
            return CodeResponse.builder()
                    .generatedCode(code)
                    .explanation(explanation)
                    .filePath(filePath)
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .type(type)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing LLM response", e);
            return CodeResponse.builder()
                    .generatedCode(response)
                    .explanation("Raw response from LLM")
                    .sessionId(sessionId)
                    .timestamp(System.currentTimeMillis())
                    .type(CodeResponse.ResponseType.CODE_GENERATION)
                    .build();
        }
    }

    private String extractJsonField(String json, String field) {
        try {
            int start = json.indexOf("\"" + field + "\":");
            if (start == -1) return "";
            
            start = json.indexOf("\"", start + field.length() + 3) + 1;
            int end = json.indexOf("\"", start);
            
            if (end == -1) {
                // Try to find end of the value
                end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                if (end == -1) end = json.length();
            }
            
            return json.substring(start, end).replace("\\\"", "\"").replace("\\n", "\n");
        } catch (Exception e) {
            log.warn("Error extracting field {} from JSON", field, e);
            return "";
        }
    }

    private String generateMockCodeBasedOnInput(String voiceInput, ProjectContext projectContext) {
        String language = projectContext.getPrimaryLanguage();
        String lowerInput = voiceInput.toLowerCase();
        
        if (lowerInput.contains("controller") || lowerInput.contains("endpoint")) {
            return generateMockController(language);
        } else if (lowerInput.contains("service")) {
            return generateMockService(language);
        } else if (lowerInput.contains("redis")) {
            return generateMockRedisService(language);
        } else if (lowerInput.contains("error") || lowerInput.contains("fix")) {
            return generateMockErrorFix(language);
        } else {
            return generateMockGenericCode(language);
        }
    }

    private String generateMockController(String language) {
        if ("java".equals(language)) {
            return """
                    @RestController
                    @RequestMapping("/api/users")
                    public class UserController {
                        
                        @Autowired
                        private UserService userService;
                        
                        @GetMapping
                        public ResponseEntity<List<User>> getAllUsers() {
                            return ResponseEntity.ok(userService.findAll());
                        }
                        
                        @PostMapping
                        public ResponseEntity<User> createUser(@RequestBody User user) {
                            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(userService.save(user));
                        }
                        
                        @PutMapping("/{id}")
                        public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
                            return ResponseEntity.ok(userService.update(id, user));
                        }
                        
                        @DeleteMapping("/{id}")
                        public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
                            userService.delete(id);
                            return ResponseEntity.noContent().build();
                        }
                    }
                    """;
        }
        return "// Mock controller code for " + language;
    }

    private String generateMockService(String language) {
        if ("java".equals(language)) {
            return """
                    @Service
                    public class UserService {
                        
                        @Autowired
                        private UserRepository userRepository;
                        
                        public List<User> findAll() {
                            return userRepository.findAll();
                        }
                        
                        public User save(User user) {
                            return userRepository.save(user);
                        }
                        
                        public User update(Long id, User user) {
                            User existingUser = userRepository.findById(id)
                                    .orElseThrow(() -> new UserNotFoundException(id));
                            existingUser.setName(user.getName());
                            existingUser.setEmail(user.getEmail());
                            return userRepository.save(existingUser);
                        }
                        
                        public void delete(Long id) {
                            userRepository.deleteById(id);
                        }
                    }
                    """;
        }
        return "// Mock service code for " + language;
    }

    private String generateMockRedisService(String language) {
        if ("java".equals(language)) {
            return """
                    @Service
                    public class RedisService {
                        
                        @Autowired
                        private RedisTemplate<String, Object> redisTemplate;
                        
                        public void setValue(String key, Object value) {
                            redisTemplate.opsForValue().set(key, value);
                        }
                        
                        public Object getValue(String key) {
                            return redisTemplate.opsForValue().get(key);
                        }
                        
                        public Set<String> getKeys(String pattern) {
                            return redisTemplate.keys(pattern);
                        }
                        
                        public void deleteKey(String key) {
                            redisTemplate.delete(key);
                        }
                    }
                    """;
        }
        return "// Mock Redis service code for " + language;
    }

    private String generateMockErrorFix(String language) {
        return """
                // Common error fixes:
                // 1. Check for null values before accessing properties
                // 2. Ensure proper exception handling
                // 3. Validate input parameters
                // 4. Check for proper resource cleanup
                
                // Example fix for null pointer exception:
                if (user != null && user.getName() != null) {
                    // Safe to access user.getName()
                }
                """;
    }

    private String generateMockGenericCode(String language) {
        return "// Generated code for " + language + "\n" +
               "// This is a placeholder for the requested functionality\n" +
               "// Please implement according to your specific requirements";
    }

    private String generateMockExplanation(String voiceInput) {
        return "Generated code based on your request: \"" + voiceInput + "\". " +
               "This code follows best practices and includes proper error handling.";
    }

    private String suggestFilePath(String voiceInput, ProjectContext projectContext) {
        String language = projectContext.getPrimaryLanguage();
        String lowerInput = voiceInput.toLowerCase();
        
        if (lowerInput.contains("controller")) {
            return "src/main/java/com/example/controller/UserController.java";
        } else if (lowerInput.contains("service")) {
            return "src/main/java/com/example/service/UserService.java";
        } else if (lowerInput.contains("repository")) {
            return "src/main/java/com/example/repository/UserRepository.java";
        } else if (lowerInput.contains("model") || lowerInput.contains("entity")) {
            return "src/main/java/com/example/model/User.java";
        }
        
        return "src/main/java/com/example/" + language + "File.java";
    }

    private CodeResponse.ResponseType determineResponseType(String voiceInput) {
        String lowerInput = voiceInput.toLowerCase();
        if (lowerInput.contains("explain")) {
            return CodeResponse.ResponseType.EXPLANATION;
        } else if (lowerInput.contains("error") || lowerInput.contains("fix")) {
            return CodeResponse.ResponseType.ERROR_FIX;
        } else if (lowerInput.contains("api") || lowerInput.contains("endpoint")) {
            return CodeResponse.ResponseType.API_SCAFFOLDING;
        } else {
            return CodeResponse.ResponseType.CODE_GENERATION;
        }
    }

    public boolean isLLMConfigured() {
        return openAiService != null;
    }
} 