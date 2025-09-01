# Security Common Module

The Security Common module provides shared security components, validation utilities, and exception handling for all EnterpriseShop microservices. This module ensures consistent security practices across the entire platform.

## 🚀 Features

- **Common Security Configuration**: Shared security settings for all services
- **Validation Framework**: Comprehensive input validation and sanitization
- **Exception Handling**: Consistent error responses across services
- **Password Security**: BCrypt password encoding and validation
- **CORS Configuration**: Cross-origin resource sharing setup
- **Input Sanitization**: Protection against XSS and injection attacks

## 🏗️ Architecture

### Components

- **Security Configuration**: Spring Security configuration classes
- **Validation Utils**: Input validation and sanitization utilities
- **Exception Handler**: Global exception handling for security issues
- **Password Encoder**: BCrypt password hashing
- **CORS Configuration**: Cross-origin request handling

### Security Layers

```
Request → CORS → Authentication → Authorization → Validation → Business Logic
   ↓
Exception Handler → Error Response
```

## 🛠️ Technology Stack

- **Spring Security**: Authentication and authorization framework
- **Bean Validation**: Input validation framework
- **BCrypt**: Password hashing algorithm
- **Spring Boot**: Application framework
- **Jakarta Validation**: Validation annotations and API

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- Spring Boot 3.x
- Spring Security 6.x

## 🚀 Usage

### 1. Add Dependency

Include this module in your service's `pom.xml`:

```xml
<dependency>
    <groupId>com.enterpriseshop</groupId>
    <artifactId>security-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. Import Security Configuration

Import the security configuration in your service:

```java
@Import(SecurityCommonConfig.class)
@SpringBootApplication
public class YourServiceApplication {
    // Your application code
}
```

### 3. Use Validation Utils

```java
@Autowired
private ValidationUtils validationUtils;

// Validate email
ValidationUtils.ValidationResult<String> emailResult = 
    ValidationUtils.validateAndSanitizeEmail(userInput.getEmail());

if (!emailResult.isValid()) {
    throw new BadRequestException(emailResult.getFirstError());
}

// Validate password
ValidationUtils.ValidationResult<String> passwordResult = 
    ValidationUtils.validatePassword(userInput.getPassword());

if (!passwordResult.isValid()) {
    throw new BadRequestException(passwordResult.getFirstError());
}
```

### 4. Use Exception Handler

The global exception handler is automatically applied when you import the security configuration.

## 🔧 Configuration

### Security Configuration

The module provides three security filter chains:

#### 1. Common Security Filter Chain
- Basic security for all services
- Public endpoints: health, info, docs
- All other endpoints require authentication

#### 2. Public Security Filter Chain
- For monitoring and health check services
- All endpoints are public
- No authentication required

#### 3. Protected Security Filter Chain
- For business logic services
- Role-based access control
- Admin and user role separation

### CORS Configuration

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // Allowed origins
    configuration.setAllowedOriginPatterns(List.of("*"));
    
    // Allowed methods
    configuration.setAllowedMethods(Arrays.asList(
        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
    ));
    
    // Allowed headers
    configuration.setAllowedHeaders(Arrays.asList(
        "Authorization", "Content-Type", "X-Requested-With"
    ));
    
    return source;
}
```

## 📡 Validation Features

### Email Validation

```java
// Basic validation
boolean isValid = ValidationUtils.isValidEmail("user@example.com");

// Validation with sanitization
ValidationResult<String> result = ValidationUtils.validateAndSanitizeEmail("user@example.com");
if (result.isValid()) {
    String sanitizedEmail = result.getData();
}
```

### Phone Validation

```java
// International phone number format
boolean isValid = ValidationUtils.isValidPhone("+1234567890");
```

### Password Validation

```java
// Comprehensive password validation
ValidationResult<String> result = ValidationUtils.validatePassword("MyP@ssw0rd");
if (!result.isValid()) {
    Map<String, String> errors = result.getErrors();
    // Handle validation errors
}
```

### UUID Validation

```java
// UUID format validation
boolean isValid = ValidationUtils.isValidUuid("550e8400-e29b-41d4-a716-446655440000");
```

### Username Validation

```java
// Username format validation
boolean isValid = ValidationUtils.isValidUsername("john_doe123");
```

### Input Sanitization

```java
// Remove potentially dangerous content
String sanitized = ValidationUtils.sanitizeInput("<script>alert('xss')</script>Hello");
// Result: "Hello"
```

## 🔐 Security Features

### Password Encoding

```java
@Autowired
private PasswordEncoder passwordEncoder;

// Encode password
String encodedPassword = passwordEncoder.encode("rawPassword");

// Verify password
boolean matches = passwordEncoder.matches("rawPassword", encodedPassword);
```

### Method-Level Security

```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() {
    // Only accessible by ADMIN role
}

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public void userOrAdminMethod() {
    // Accessible by USER or ADMIN role
}

@PreAuthorize("#userId == authentication.principal.userId")
public void ownResourceMethod(String userId) {
    // Users can only access their own resources
}
```

### Custom Security Expressions

```java
@PreAuthorize("@securityService.canAccessResource(#resourceId)")
public void customSecurityMethod(String resourceId) {
    // Custom security logic
}
```

## 📊 Exception Handling

### Validation Errors

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Validation failed for the request",
  "details": {
    "email": "Invalid email format",
    "password": "Password must be at least 8 characters long"
  },
  "path": "/api/users"
}
```

### Authentication Errors

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 401,
  "error": "Authentication Failed",
  "message": "Authentication failed: Invalid token",
  "path": "/api/users"
}
```

### Access Denied Errors

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 403,
  "error": "Access Denied",
  "message": "You don't have permission to access this resource",
  "path": "/api/admin/users"
}
```

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

### Security Tests

```java
@SpringBootTest
@AutoConfigureTestDatabase
class SecurityTest {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    void testPasswordEncoding() {
        String rawPassword = "testPassword123!";
        String encoded = passwordEncoder.encode(rawPassword);
        
        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches(rawPassword, encoded));
    }
    
    @Test
    void testEmailValidation() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertFalse(ValidationUtils.isValidEmail("invalid-email"));
    }
}
```

## 🚀 Integration

### With Auth Service

```java
@Configuration
@Import(SecurityCommonConfig.class)
public class AuthServiceSecurityConfig {
    
    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {
        // Custom auth service security configuration
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

### With User Service

```java
@Configuration
@Import(SecurityCommonConfig.class)
public class UserServiceSecurityConfig {
    
    @Bean
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        // Custom user service security configuration
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

## 🔍 Troubleshooting

### Common Issues

1. **Security Configuration Not Applied**
   - Ensure `@Import(SecurityCommonConfig.class)` is used
   - Check for conflicting security configurations
   - Verify Spring Security dependencies

2. **Validation Not Working**
   - Ensure `@Valid` annotations are used
   - Check that validation dependencies are included
   - Verify validation groups if used

3. **CORS Issues**
   - Check CORS configuration in security config
   - Verify allowed origins and methods
   - Check browser console for CORS errors

4. **Password Encoding Issues**
   - Ensure BCrypt dependency is included
   - Check password encoder bean configuration
   - Verify password strength requirements

### Debug Mode

Enable debug logging for security:

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    com.enterpriseshop.security: DEBUG
```

## 📚 Best Practices

### 1. Security Configuration

- Use appropriate security filter chain for your service type
- Implement role-based access control
- Use method-level security annotations
- Regularly review and update security policies

### 2. Input Validation

- Always validate and sanitize user input
- Use Bean Validation annotations on DTOs
- Implement custom validation for business rules
- Sanitize input to prevent XSS attacks

### 3. Password Security

- Use strong password requirements
- Implement password history
- Use secure password reset mechanisms
- Monitor for password-related security events

### 4. Exception Handling

- Log security exceptions for monitoring
- Don't expose sensitive information in error messages
- Use consistent error response format
- Implement proper error codes

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is part of the EnterpriseShop platform and follows the same licensing terms.

## 🆘 Support

For support and questions:

- Check the logs for error details
- Review the security configuration
- Consult the EnterpriseShop documentation
- Open an issue in the repository

---

**Security Common Module** - Part of the EnterpriseShop Microservices Platform
