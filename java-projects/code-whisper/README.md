# CodeWhisperer - Context-Aware Voice Chat for Developers

🔥 **A powerful backend system that powers voice-controlled, real-time assistance for developers.**

CodeWhisperer listens to voice commands, understands your project context, and responds with generated code snippets, explanations, or API scaffolding. It's like combining ChatGPT + Speech-to-Text + Dev tooling in a developer backend.

## 🌟 Features

- **🎙️ Voice Input Processing** - Convert voice commands to text
- **📚 Project Context Analysis** - Automatically scan and understand your project structure
- **🧠 AI-Powered Code Generation** - Generate context-aware code using OpenAI
- **📤 Real-time Updates** - WebSocket-based live feedback
- **💾 Conversation History** - Save and retrieve past interactions
- **🔧 Git Integration** - Understand your current branch and recent commits
- **📊 Project Metadata** - Analyze dependencies, file structure, and more

## 🛠️ Tech Stack

- **Java 17** with **Spring Boot 3.2.0**
- **WebSocket** for real-time communication
- **MongoDB** for conversation persistence
- **OpenAI GPT-4** for code generation
- **JGit** for Git integration
- **Maven** for dependency management

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB (optional, for conversation history)
- OpenAI API key (optional, for real code generation)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd code-whisper
   ```

2. **Configure environment variables**
   ```bash
   export OPENAI_API_KEY=your_openai_api_key_here
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Voice Processing

#### Process Voice Request
```http
POST /api/voice/process
Content-Type: application/json

{
  "audioData": "base64_encoded_audio_data",
  "projectPath": "/path/to/your/project",
  "language": "java",
  "sessionId": "optional_session_id"
}
```

**Response:**
```json
{
  "generatedCode": "// Generated code here",
  "explanation": "Explanation of the generated code",
  "filePath": "suggested/file/path.java",
  "language": "java",
  "sessionId": "session_id",
  "timestamp": 1234567890,
  "type": "CODE_GENERATION"
}
```

#### Get Project Context
```http
GET /api/voice/project/{projectPath}
```

#### Get Conversation History
```http
GET /api/voice/history/{sessionId}
```

#### Service Status
```http
GET /api/voice/status
```

#### Health Check
```http
GET /api/voice/health
```

### WebSocket Endpoints

- **Connect:** `ws://localhost:8080/ws`
- **Subscribe to session:** `/app/subscribe`
- **Receive updates:** `/topic/session/{sessionId}`
- **Ping/Pong:** `/app/ping` → `/topic/pong`

## 🎯 Example Use Cases

### 1. Generate a Spring Boot Controller
**Voice Command:** "Create a REST controller for user management"

**Response:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    // ... more endpoints
}
```

### 2. Create a Service Class
**Voice Command:** "Generate a service that connects to Redis"

**Response:**
```java
@Service
public class RedisService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    // ... more methods
}
```

### 3. Fix Code Errors
**Voice Command:** "Explain this error in my code"

**Response:**
```java
// Common error fixes:
// 1. Check for null values before accessing properties
// 2. Ensure proper exception handling
// 3. Validate input parameters

if (user != null && user.getName() != null) {
    // Safe to access user.getName()
}
```

## ⚙️ Configuration

### Application Properties

Key configuration options in `application.yml`:

```yaml
codewhisperer:
  openai:
    api-key: ${OPENAI_API_KEY:}
    model: gpt-4
    max-tokens: 2000
  
  voice:
    provider: mock  # mock, whisper, google-stt
  
  project:
    max-file-count: 100
    excluded-dirs:
      - target
      - node_modules
      - .git
```

### Environment Variables

- `OPENAI_API_KEY` - Your OpenAI API key
- `MONGODB_URI` - MongoDB connection string (optional)

## 🔧 Development

### Project Structure
```
src/main/java/com/codewhisperer/
├── CodeWhispererApplication.java
├── config/
│   ├── WebSocketConfig.java
│   └── AppConfig.java
├── controller/
│   ├── VoiceController.java
│   └── WebSocketController.java
├── model/
│   ├── VoiceRequest.java
│   ├── CodeResponse.java
│   ├── ProjectContext.java
│   └── ConversationHistory.java
└── service/
    ├── CodeWhispererService.java
    ├── VoiceToTextService.java
    ├── ProjectScannerService.java
    ├── LLMService.java
    └── ConversationHistoryService.java
```

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
java -jar target/code-whisperer-1.0.0.jar
```

## 🎨 Frontend Integration

### WebSocket Client Example (JavaScript)
```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected to CodeWhisperer');
    
    // Subscribe to session updates
    stompClient.subscribe('/topic/session/' + sessionId, function (update) {
        const data = JSON.parse(update.body);
        console.log('Update:', data.message, data.status);
    });
});
```

### REST API Client Example
```javascript
const response = await fetch('/api/voice/process', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        audioData: base64AudioData,
        projectPath: '/path/to/project',
        language: 'java',
        sessionId: 'session-123'
    })
});

const result = await response.json();
console.log('Generated code:', result.generatedCode);
```

## 🚀 Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/code-whisperer-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Setup
```bash
# Production environment variables
export OPENAI_API_KEY=your_production_key
export MONGODB_URI=mongodb://production-db:27017/codewhisperer
export SPRING_PROFILES_ACTIVE=production
```

## 🔮 Future Enhancements

- [ ] **Real Whisper API Integration** - Replace mock voice processing
- [ ] **VSCode Extension Backend** - Direct IDE integration
- [ ] **GitHub Integration** - Pull request generation
- [ ] **Plugin System** - Extensible architecture
- [ ] **Multi-language Support** - Beyond Java
- [ ] **Code Review Assistant** - Automated code review suggestions
- [ ] **Performance Analytics** - Usage tracking and optimization

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Issues:** [GitHub Issues](https://github.com/your-repo/code-whisperer/issues)
- **Documentation:** [Wiki](https://github.com/your-repo/code-whisperer/wiki)
- **Discussions:** [GitHub Discussions](https://github.com/your-repo/code-whisperer/discussions)

---

**Made with ❤️ for developers who love to code with their voice!** 