# EnterpriseShop - Enterprise-Grade E-Commerce Microservices Platform

EnterpriseShop is a comprehensive, enterprise-grade e-commerce backend built with Java microservices architecture. This platform demonstrates modern software engineering practices, scalable architecture patterns, and production-ready features.

## 🏗️ Architecture Overview

EnterpriseShop follows a microservices architecture with the following core services:

- **Auth Service** (Port: 8081) - Authentication & Authorization with JWT
- **User Service** (Port: 8082) - User Management with REST & GraphQL
- **Product Service** (Port: 8083) - Product Catalog Management
- **Order Service** (Port: 8084) - Order Processing & Management
- **Inventory Service** (Port: 8085) - Inventory Tracking
- **Payment Service** (Port: 8086) - Payment Processing
- **Notification Service** (Port: 8087) - Email, SMS, Push Notifications
- **Support Service** (Port: 8088) - Customer Support & Chat
- **API Gateway** (Port: 8080) - Request Routing & Rate Limiting
- **Config Server** (Port: 8888) - Centralized Configuration

## 🚀 Technology Stack

### Core Technologies
- **Java**: 21 (LTS)
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **Spring Security**: JWT Authentication
- **Spring Data JPA**: Database Operations
- **Maven**: Build Tool

### Databases
- **PostgreSQL**: Primary relational database
- **MongoDB**: Document database for flexible schemas
- **Neo4j**: Graph database for recommendations
- **Elasticsearch**: Search and analytics
- **Redis**: Caching and session storage

### Messaging
- **Apache Kafka**: Event streaming
- **RabbitMQ**: Message queuing

### Infrastructure
- **Docker**: Containerization
- **Docker Compose**: Local development
- **Kubernetes**: Production orchestration (planned)

### Monitoring & Observability
- **Prometheus**: Metrics collection
- **Grafana**: Metrics visualization
- **ELK Stack**: Logging (Elasticsearch, Logstash, Kibana)
- **Jaeger**: Distributed tracing

## 📋 Project Status

### ✅ Completed (Phase 1)
- [x] Project structure and documentation
- [x] Maven parent project with dependency management
- [x] Docker Compose development environment
- [x] **Auth Service** - Complete with JWT authentication
- [x] Database initialization scripts
- [x] Monitoring infrastructure setup

### 🔄 In Progress (Phase 2)
- [ ] User Service implementation
- [ ] API Gateway setup
- [ ] Kafka and RabbitMQ integration
- [ ] Event-driven architecture

### ⏳ Planned
- [ ] Product, Order, Inventory Services
- [ ] Payment integration
- [ ] Notification system
- [ ] Support service with WebRTC
- [ ] CI/CD pipeline
- [ ] Kubernetes deployment

## 🛠️ Quick Start

### Prerequisites
- Java 21
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. Clone the Repository
```bash
git clone <repository-url>
cd EnterpriseShop
```

### 2. Start Infrastructure Services
```bash
# Start all infrastructure services (PostgreSQL, Redis, Kafka, etc.)
docker-compose up -d
```

### 3. Initialize Database
```bash
# The database will be automatically initialized when PostgreSQL starts
# Check logs to confirm initialization
docker-compose logs postgres
```

### 4. Build and Run Auth Service
```bash
# Build the auth service
cd auth-service
mvn clean package

# Run the service
java -jar target/auth-service-1.0.0-SNAPSHOT.jar
```

### 5. Test the Service
```bash
# Health check
curl http://localhost:8081/auth/actuator/health

# Register a new user
curl -X POST http://localhost:8081/auth/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'

# Login
curl -X POST http://localhost:8081/auth/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "password123"
  }'
```

## 📊 Monitoring & Observability

### Access Points
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Kibana**: http://localhost:5601
- **Jaeger**: http://localhost:16686
- **RabbitMQ Management**: http://localhost:15672 (admin/admin)
- **Neo4j Browser**: http://localhost:7474 (neo4j/password)

### Health Checks
- Auth Service: http://localhost:8081/auth/actuator/health
- Prometheus Metrics: http://localhost:8081/auth/actuator/prometheus

## 🏛️ Architecture Patterns

### Microservices
- **Service Independence**: Each service has its own database and can be deployed independently
- **API Gateway**: Centralized routing, authentication, and rate limiting
- **Service Discovery**: Netflix Eureka for service registration and discovery

### Event-Driven Architecture
- **Kafka**: High-throughput event streaming for order events, payment events
- **RabbitMQ**: Reliable message queuing for notifications and background tasks
- **Event Sourcing**: Store events as the source of truth

### Data Management
- **Polyglot Persistence**: Different databases for different use cases
- **CQRS**: Separate read and write models for scalability
- **Saga Pattern**: Distributed transaction management

### Security
- **JWT Authentication**: Stateless authentication with access and refresh tokens
- **OAuth2**: Authorization framework for third-party integrations
- **Role-Based Access Control**: Fine-grained permissions

## 📚 Learning Resources

This project serves as a comprehensive learning platform for:

- **Microservices Architecture**: Design patterns, communication, deployment
- **Spring Boot & Spring Cloud**: Modern Java development
- **Event-Driven Systems**: Kafka, RabbitMQ, event sourcing
- **Database Design**: Polyglot persistence, CQRS, saga patterns
- **Security**: JWT, OAuth2, RBAC
- **DevOps**: Docker, Kubernetes, CI/CD
- **Observability**: Monitoring, logging, tracing

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific service tests
cd auth-service && mvn test

# Run with coverage
mvn jacoco:report
```

### Test Data
The database initialization script includes sample data:
- **Admin User**: admin/admin123
- **Regular User**: user/user123
- **Sample Products**: Electronics, Clothing, Books
- **Sample Orders**: Test orders with various statuses

## 🚀 Deployment

### Development
```bash
# Start all services
docker-compose up -d

# Build and run individual services
mvn clean package
java -jar target/service-name.jar
```

### Production (Planned)
- Kubernetes manifests in `k8s/` directory
- Helm charts for easy deployment
- CI/CD pipeline with GitHub Actions
- Blue-green deployment strategy

## 📝 API Documentation

### Auth Service API
- **Base URL**: http://localhost:8081/auth
- **Swagger UI**: http://localhost:8081/auth/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8081/auth/v3/api-docs

### API Endpoints
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User authentication
- `POST /api/auth/refresh` - Token refresh
- `POST /api/auth/logout` - User logout
- `GET /api/auth/me` - Current user info

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: Check the individual service README files
- **Issues**: Create an issue in the GitHub repository
- **Discussions**: Use GitHub Discussions for questions and ideas

## 🎯 Roadmap

### Phase 2: Messaging Infrastructure
- [ ] Kafka cluster setup and configuration
- [ ] RabbitMQ integration
- [ ] Event publishing and consuming
- [ ] Outbox pattern implementation

### Phase 3: Core Business Services
- [ ] User Service with GraphQL
- [ ] Product Service with MongoDB
- [ ] Order Service with event sourcing
- [ ] Inventory Service with Neo4j

### Phase 4: Advanced Features
- [ ] WebRTC support service
- [ ] Real-time notifications
- [ ] Advanced search with Elasticsearch
- [ ] AI/ML integration

### Phase 5: Production Readiness
- [ ] Kubernetes deployment
- [ ] CI/CD pipeline
- [ ] Performance testing
- [ ] Security hardening

---

**EnterpriseShop** - Building the future of e-commerce, one microservice at a time! 🚀
