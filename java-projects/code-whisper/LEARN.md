# 🎓 CodeWhisperer Learning Guide
## From Java Fundamentals to Spring Boot Development

Welcome! This guide explains everything about building enterprise Java applications using Spring Boot.

---

## 📁 Project Structure Explained

### Why This Structure?
```
code-whisper/
├── pom.xml                          # 🏗️ Maven configuration (like package.json)
├── src/main/java/com/codewhisperer/ # ☕ Java source code
│   ├── CodeWhispererApplication.java # 🚀 Main entry point
│   ├── config/                      # ⚙️ Configuration classes
│   ├── controller/                  # 🌐 REST API endpoints
│   ├── model/                       # 📊 Data structures
│   └── service/                     # 🧠 Business logic
├── src/main/resources/              # 📄 Configuration files
│   ├── application.yml              # ⚙️ App settings
│   └── static/                      # 🌐 Frontend files
└── target/                          # 🎯 Compiled output
```

**Why packages?** `com.codewhisperer.*`
- **Namespace**: Avoid naming conflicts
- **Organization**: Group related classes
- **Access control**: Control visibility between packages

---

## 🏗️ Maven (Build Tool)

### What is Maven?
Maven is like `npm` for Java - manages dependencies, compiles code, runs tests, packages applications.

### pom.xml Explained
```xml
<project>
    <groupId>com.codewhisperer</groupId>        <!-- Organization ID -->
    <artifactId>code-whisperer</artifactId>     <!-- Project name -->
    <version>1.0.0</version>                    <!-- Version -->
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>
    
    <dependencies>
        <!-- Spring Boot Web (REST APIs) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- MongoDB (database) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
    </dependencies>
</project>
```

### Maven Commands
```bash
mvn clean          # 🧹 Clean compiled files
mvn compile        # 🔨 Compile source code
mvn test           # 🧪 Run tests
mvn package        # 📦 Create JAR file
mvn spring-boot:run # 🚀 Run Spring Boot app
```

---

## 🌱 Spring Boot Framework

### What is Spring Boot?
A framework that makes it easy to create production-ready Java applications with minimal configuration.

### Key Features
- **Auto-configuration**: Sets up common patterns automatically
- **Embedded servers**: No need for external Tomcat/Jetty
- **Starter dependencies**: Easy to add features
- **Production-ready**: Health checks, metrics, monitoring

### Application Entry Point
```java
@SpringBootApplication                    // 🏷️ Marks as Spring Boot app
public class CodeWhispererApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CodeWhispererApplication.class, args); // 🚀 Start app
    }
}
```

### Spring Annotations
```java
@SpringBootApplication    // Main application class
@Component               // Spring component
@Service                 // Business logic component
@Repository              // Data access component
@Controller              // Web controller
@RestController          // REST API controller
@Configuration           // Configuration class
@Autowired               // Inject dependencies
@Value                   // Inject config values
```

---

## 🏛️ Project Architecture

### Layered Architecture
```
┌─────────────────────────────────────┐
│           Controllers               │ ← REST API endpoints
├─────────────────────────────────────┤
│            Services                 │ ← Business logic
├─────────────────────────────────────┤
│          Repositories               │ ← Database access
├─────────────────────────────────────┤
│            Database                 │ ← MongoDB
└─────────────────────────────────────┘
```

### Layer Responsibilities

#### 1. **Controllers** (REST APIs)
```java
@RestController
@RequestMapping("/api/voice")
public class VoiceController {
    
    @Autowired
    private CodeWhispererService service;
    
    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<CodeResponse>> 
           processVoiceRequest(@RequestBody VoiceRequest request) {
        // Handle HTTP requests
        // Call business logic
        // Return HTTP response
    }
}
```

#### 2. **Services** (Business Logic)
```java
@Service
public class CodeWhispererService {
    
    @Autowired
    private VoiceToTextService voiceService;
    
    public CompletableFuture<CodeResponse> processVoiceRequest(VoiceRequest request) {
        // Orchestrate business logic
        // Coordinate between services
        // Handle business rules
    }
}
```

#### 3. **Models** (Data Structures)
```java
@Data
@Builder
public class VoiceRequest {
    private String audioData;      // Base64 encoded audio
    private String projectPath;    // Path to project
    private String language;       // Programming language
    private String sessionId;      // Session identifier
}
```

---

## 🗄️ Database & MongoDB

### Why MongoDB?
- **NoSQL**: Flexible document storage
- **JSON-like**: Natural for web applications
- **Spring Data**: Easy integration with Spring Boot

### Spring Data MongoDB
```java
@Document(collection = "conversations")
public class ConversationHistory {
    @Id
    private String id;                    // MongoDB document ID
    private String sessionId;             // Session identifier
    private String projectPath;           // Project path
    private List<ConversationEntry> entries; // Conversation entries
}
```

### Service with MongoDB
```java
@Service
public class ConversationHistoryService {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public ConversationHistory saveConversation(String sessionId, String projectPath) {
        ConversationHistory conversation = ConversationHistory.builder()
                .sessionId(sessionId)
                .projectPath(projectPath)
                .createdAt(LocalDateTime.now())
                .build();
        
        return mongoTemplate.save(conversation);  // Save to MongoDB
    }
}
```

---

## 🌐 REST API Development

### HTTP Methods
```java
@GetMapping("/health")           // GET - Retrieve data
@PostMapping("/process")         // POST - Create new resource
@PutMapping("/{id}")            // PUT - Update existing resource
@DeleteMapping("/{id}")         // DELETE - Remove resource
```

### Request/Response Flow
```
Client Request → Controller → Service → Repository → Database
                ↓
Client Response ← Controller ← Service ← Repository ← Database
```

### API Endpoints
```http
GET /api/voice/health
Response: "CodeWhisperer is running!"

POST /api/voice/process
Body: {
  "audioData": "base64_encoded_audio",
  "projectPath": "/path/to/project",
  "language": "java",
  "sessionId": "session-123"
}
```

### Response Handling
```java
@PostMapping("/process")
public CompletableFuture<ResponseEntity<CodeResponse>> processVoiceRequest(
        @Valid @RequestBody VoiceRequest request) {
    
    return codeWhispererService.processVoiceRequest(request)
            .thenApply(response -> ResponseEntity.ok(response))  // 200 OK
            .exceptionally(throwable -> 
                ResponseEntity.internalServerError()             // 500 Error
                    .body(CodeResponse.builder()
                        .explanation("Error: " + throwable.getMessage())
                        .build()));
}
```

---

## 📡 WebSocket (Real-time Communication)

### Why WebSocket?
- **Real-time updates**: No need to poll server
- **Bidirectional**: Server can push data to client
- **Efficient**: Single connection, low overhead

### WebSocket Configuration
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");           // Broadcast to topics
        config.setApplicationDestinationPrefixes("/app"); // Client messages
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")                    // WebSocket endpoint
                .setAllowedOriginPatterns("*")         // CORS configuration
                .withSockJS();                         // SockJS fallback
    }
}
```

### Real-time Updates
```java
@Service
public class CodeWhispererService {
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    private void sendWebSocketUpdate(String sessionId, String message, String status) {
        WebSocketUpdate update = WebSocketUpdate.builder()
                .sessionId(sessionId)
                .message(message)
                .status(status)
                .timestamp(System.currentTimeMillis())
                .build();
        
        // Send to specific session topic
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, update);
    }
}
```

---

## 🎨 Frontend Integration

### Static Resources
Spring Boot serves static files from `src/main/resources/static/`:
- HTML files
- CSS stylesheets
- JavaScript files
- Images

### HTML Interface
```html
<!DOCTYPE html>
<html>
<head>
    <title>CodeWhisperer</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
</head>
<body>
    <div class="container">
        <h1>🎙️ CodeWhisperer</h1>
        <div class="section">
            <h2>Voice Request Processing</h2>
            <input type="text" id="projectPath" placeholder="Project Path">
            <button onclick="processVoiceRequest()">Process Voice Request</button>
            <div id="output"></div>
        </div>
    </div>
    
    <script>
        async function processVoiceRequest() {
            const response = await fetch('/api/voice/process', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    audioData: 'dGVzdCBhdWRpbyBkYXRh',
                    projectPath: document.getElementById('projectPath').value,
                    language: 'java',
                    sessionId: 'test-session-123'
                })
            });
            
            const result = await response.json();
            document.getElementById('output').textContent = JSON.stringify(result, null, 2);
        }
    </script>
</body>
</html>
```

---

## 🔄 Development Workflow

### 1. **Development Cycle**
```bash
# 1. Make changes to code
# 2. Compile and test
mvn compile
mvn test

# 3. Run application
mvn spring-boot:run

# 4. Test in browser
open http://localhost:8080

# 5. Repeat
```

### 2. **Debugging**
```java
@Slf4j  // Lombok annotation for logging
@Service
public class CodeWhispererService {
    
    public CompletableFuture<CodeResponse> processVoiceRequest(VoiceRequest request) {
        log.info("Processing voice request for session: {}", request.getSessionId());
        
        try {
            // Your code here
            log.info("Request processed successfully");
        } catch (Exception e) {
            log.error("Error processing request", e);
            throw e;
        }
    }
}
```

### 3. **Configuration**
```yaml
# application.yml
server:
  port: 8080

spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: codewhisperer

codewhisperer:
  openai:
    api-key: ${OPENAI_API_KEY:}  # Environment variable
    model: gpt-4
```

---

## 🚀 Deployment

### 1. **Create JAR File**
```bash
mvn clean package
# Creates: target/code-whisperer-1.0.0.jar
```

### 2. **Run JAR**
```bash
java -jar target/code-whisperer-1.0.0.jar
```

### 3. **Environment Variables**
```bash
export OPENAI_API_KEY=your_api_key
export SPRING_PROFILES_ACTIVE=production
java -jar code-whisperer-1.0.0.jar
```

### 4. **Docker**
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/code-whisperer-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## 📈 Learning Path

### Week 1: Foundation
- [ ] Understand Maven and project structure
- [ ] Learn Spring Boot basics
- [ ] Practice with REST APIs
- [ ] Explore the codebase

### Week 2: Advanced Concepts
- [ ] Study WebSocket communication
- [ ] Learn MongoDB integration
- [ ] Understand dependency injection
- [ ] Practice with configuration

### Week 3: Real Integration
- [ ] Get OpenAI API key and integrate
- [ ] Set up MongoDB locally
- [ ] Add real voice processing
- [ ] Implement error handling

### Week 4: Production Ready
- [ ] Add comprehensive testing
- [ ] Implement security measures
- [ ] Set up monitoring and logging
- [ ] Deploy to cloud platform

---

## 🎯 Key Concepts Summary

### What You've Learned
1. **Enterprise Java Development**: Project structure, build tools, frameworks
2. **Spring Boot**: Auto-configuration, starters, annotations
3. **REST APIs**: HTTP methods, request/response handling
4. **Real-time Communication**: WebSocket, STOMP protocol
5. **Database Integration**: MongoDB, Spring Data
6. **Full-stack Development**: Backend APIs + Frontend interface

### Core Principles
- **Layered Architecture**: Separation of concerns
- **Dependency Injection**: Loose coupling between components
- **Configuration Management**: Environment-specific settings
- **Error Handling**: Graceful failure management
- **Testing**: Unit and integration testing
- **Deployment**: Production-ready applications

---

## 🚀 Ready to Build!

You now have a solid foundation in enterprise Java development with Spring Boot. The CodeWhisperer project demonstrates real-world patterns and practices.

**Remember**: The best way to learn is by doing. Start with small modifications to this project, then build your own applications.

Happy coding! 🎉 