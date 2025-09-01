# Config Server

The Config Server provides centralized configuration management for all EnterpriseShop microservices. It supports Git-based configuration storage, environment-specific configurations, and automatic configuration refresh.

## 🚀 Features

- **Centralized Configuration**: Single source of truth for all service configurations
- **Git Integration**: Version-controlled configuration management
- **Environment Support**: Dev, test, staging, and production profiles
- **Auto-refresh**: Hot reloading of configuration changes
- **Encryption**: Secure storage of sensitive configuration values
- **Health Monitoring**: Comprehensive health checks and metrics
- **Service Discovery**: Integration with Eureka service registry

## 🏗️ Architecture

### Components

- **Configuration Repository**: Git-based storage for configurations
- **Environment Repository**: Profile-specific configuration management
- **Encryption Service**: Secure handling of sensitive data
- **Health Checks**: Configuration validation and monitoring
- **REST API**: Configuration retrieval and management endpoints

### Configuration Flow

```
Service Request → Config Server → Git Repository → Environment Resolution → Configuration Response
                ↓
            Cache Layer (Redis)
```

## 🛠️ Technology Stack

- **Spring Cloud Config Server**: Configuration management framework
- **Spring Boot**: Application framework
- **Spring Cloud Netflix**: Service discovery with Eureka
- **Git**: Configuration version control
- **Micrometer**: Metrics and monitoring
- **Docker**: Containerization

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- Git repository for configurations
- Eureka Server (for service discovery)
- Docker (optional)

## 🚀 Quick Start

### 1. Clone and Build

```bash
cd EnterpriseShop/config-server
mvn clean package
```

### 2. Run with Maven

```bash
mvn spring-boot:run
```

### 3. Run with Docker

```bash
docker build -t config-server .
docker run -p 8888:8888 config-server
```

### 4. Run with Docker Compose

```bash
cd EnterpriseShop
docker-compose up config-server
```

## 🔧 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `GIT_URI` | `https://github.com/your-org/enterpriseshop-config.git` | Git repository URL |
| `GIT_BRANCH` | `main` | Default Git branch |
| `GIT_SEARCH_PATHS` | `config` | Configuration file search paths |
| `EUREKA_URL` | `http://localhost:8761/eureka/` | Eureka server URL |

### Application Properties

The Config Server uses Spring Cloud Config with:

- **Port**: 8888
- **Git Backend**: Primary configuration storage
- **Native Backend**: Local configuration fallback
- **Service Discovery**: Eureka client enabled
- **Health Monitoring**: Actuator endpoints enabled

## 📡 API Endpoints

### Configuration Endpoints

#### Get Configuration
```bash
# Get configuration for application and profile
GET /{application}/{profile}

# Get configuration with specific label
GET /{application}/{profile}/{label}

# Get configuration properties
GET /{application}/properties?profile={profile}&label={label}
```

#### Configuration Management
```bash
# Validate configuration
POST /{application}/validate?profile={profile}&label={label}

# Refresh configuration
POST /{application}/refresh?profile={profile}&label={label}

# Get server information
GET /info

# Get available applications
GET /applications

# Get available profiles
GET /profiles
```

### Actuator Endpoints

- **Health**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **Configuration Properties**: `/actuator/configprops`
- **Environment**: `/actuator/env`

## 🗄️ Configuration Structure

### Git Repository Structure

```
enterpriseshop-config/
├── config/
│   ├── auth-service/
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   ├── application-test.yml
│   │   └── application-prod.yml
│   ├── user-service/
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   ├── application-test.yml
│   │   └── application-prod.yml
│   ├── api-gateway/
│   │   ├── application.yml
│   │   ├── application-dev.yml
│   │   ├── application-test.yml
│   │   └── application-prod.yml
│   └── config-server/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-test.yml
│       └── application-prod.yml
```

### Configuration File Example

```yaml
# config/auth-service/application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth
    username: ${DB_USERNAME:enterpriseshop_user}
    password: ${DB_PASSWORD:enterpriseshop_pass}
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8081

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus

# Custom properties
app:
  jwt:
    secret: ${JWT_SECRET:your-secret-key}
    expiration: 86400000
  security:
    cors:
      allowed-origins: "*"
```

## 🔐 Security

### Configuration Encryption

Sensitive configuration values can be encrypted:

```yaml
# Encrypted password
spring:
  datasource:
    password: '{cipher}AQA...'
```

### Encryption Setup

```bash
# Generate encryption key
echo "your-encryption-key" | base64

# Encrypt value
curl -X POST "http://localhost:8888/encrypt" \
     -H "Content-Type: text/plain" \
     -d "your-secret-value"
```

## 📊 Monitoring

### Health Checks

- **Configuration Repository**: Git connectivity
- **Environment Resolution**: Profile loading
- **Service Discovery**: Eureka integration
- **Configuration Validation**: Property validation

### Metrics

- Configuration request counts
- Response times
- Error rates
- Cache hit/miss ratios

### Alerts

- Configuration repository unavailable
- High error rates
- Slow response times
- Configuration validation failures

## 🔄 Configuration Refresh

### Manual Refresh

```bash
# Refresh specific application
curl -X POST "http://localhost:8888/actuator/refresh" \
     -H "Content-Type: application/json" \
     -d '{"name": "auth-service"}'
```

### Auto-refresh

Services can be configured to automatically refresh configurations:

```yaml
# In service application.yml
spring:
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
      retry:
        initial-interval: 1000
        max-interval: 2000
        max-attempts: 6
```

## 🧪 Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests

```bash
mvn test -Dtest=*IntegrationTest
```

### Manual Testing

```bash
# Test configuration retrieval
curl http://localhost:8888/auth-service/default

# Test configuration validation
curl -X POST "http://localhost:8888/config/auth-service/validate?profile=dev"

# Test configuration refresh
curl -X POST "http://localhost:8888/config/auth-service/refresh?profile=dev"
```

## 🚀 Deployment

### Docker

```bash
# Build image
docker build -t config-server:latest .

# Run container
docker run -d \
  --name config-server \
  -p 8888:8888 \
  -e GIT_URI=https://github.com/your-org/enterpriseshop-config.git \
  -e EUREKA_URL=http://eureka:8761/eureka/ \
  config-server:latest
```

### Kubernetes

```bash
# Apply manifests
kubectl apply -f k8s/

# Check deployment
kubectl get pods -l app=config-server
```

### Environment Variables

```bash
export GIT_URI=https://github.com/your-org/enterpriseshop-config.git
export GIT_BRANCH=main
export EUREKA_URL=http://your-eureka:8761/eureka/
```

## 🔍 Troubleshooting

### Common Issues

1. **Git Repository Unavailable**
   - Check Git repository URL
   - Verify network connectivity
   - Check authentication credentials

2. **Configuration Not Loading**
   - Verify application name and profile
   - Check Git branch and search paths
   - Validate configuration file format

3. **Service Discovery Failed**
   - Check Eureka server connectivity
   - Verify service registration
   - Check network configuration

4. **Configuration Refresh Not Working**
   - Verify actuator endpoints enabled
   - Check refresh endpoint configuration
   - Monitor application logs

### Logs

```bash
# View logs
docker logs config-server

# Follow logs
docker logs -f config-server

# Check specific errors
docker logs config-server | grep ERROR
```

### Health Check

```bash
# Check service health
curl http://localhost:8888/actuator/health

# Check configuration repository health
curl http://localhost:8888/actuator/health/config
```

## 📚 Configuration Best Practices

### 1. Environment Separation

- Use separate profiles for each environment
- Keep sensitive data in environment variables
- Use encryption for secrets in Git

### 2. Configuration Organization

- Group related properties logically
- Use consistent naming conventions
- Document configuration options

### 3. Security

- Never commit secrets to Git
- Use encryption for sensitive values
- Implement proper access controls

### 4. Monitoring

- Monitor configuration changes
- Track configuration usage
- Alert on configuration failures

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
- Review the configuration
- Consult the EnterpriseShop documentation
- Open an issue in the repository

---

**Config Server** - Part of the EnterpriseShop Microservices Platform
