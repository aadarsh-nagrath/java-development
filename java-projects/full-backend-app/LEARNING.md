# EnterpriseShop Learning Journey

## 📚 What We're Learning

This document tracks all the concepts, technologies, and architectural patterns we're learning while building the EnterpriseShop Java backend project. Each section will be updated as we implement and learn new concepts.

## 🏗️ Architecture & Design Patterns

### Microservices Architecture
- **What**: Building applications as a collection of small, independent services
- **Why**: Scalability, maintainability, technology diversity, team autonomy
- **How**: Each service has its own database, runs independently, communicates via APIs
- **Learning Status**: 🔄 In Progress
- **Implementation**: Building separate services for auth, users, products, orders, etc.

### Event-Driven Architecture
- **What**: Services communicate through events rather than direct API calls
- **Why**: Loose coupling, scalability, fault tolerance
- **How**: Using Kafka for event streaming, RabbitMQ for task queues
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 2

### CQRS (Command Query Responsibility Segregation)
- **What**: Separating read and write operations into different models
- **Why**: Performance optimization, scalability, complex business logic
- **How**: Commands for writes, Queries for reads, separate data models
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 7 with Axon Framework

### Saga Pattern
- **What**: Managing distributed transactions across multiple services
- **Why**: Maintain data consistency without distributed transactions
- **How**: Choreography or orchestration of compensating actions
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 3 for order processing

### Outbox Pattern
- **What**: Ensuring reliable message publishing by storing events in database first
- **Why**: Guaranteed message delivery, consistency between database and messaging
- **How**: Store events in database, background job publishes to message broker
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 2

## 🚀 Spring Framework & Ecosystem

### Spring Boot 3
- **What**: Framework for building production-ready applications
- **Why**: Auto-configuration, embedded servers, production-ready features
- **How**: Using Spring Boot 3 with Java 21
- **Learning Status**: ✅ Completed
- **Implementation**: Auth-service implemented with Spring Boot 3, Java 21, and Spring Security
- **Key Learnings**: 
  - Spring Boot auto-configuration for databases, security, and monitoring
  - Application properties and profiles for different environments
  - Actuator endpoints for health checks and metrics
  - Maven dependency management and build lifecycle

### Spring Cloud
- **What**: Tools for building common distributed system patterns
- **Why**: Service discovery, configuration management, circuit breakers
- **How**: Netflix Eureka, Spring Cloud Config, Resilience4J
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented in API Gateway with Spring Cloud Gateway and Eureka, and Config Server with Spring Cloud Config
- **Key Learnings**:
  - Spring Cloud Gateway for API routing and filtering
  - Service discovery with Eureka client
  - Circuit breaker patterns with Resilience4j
  - Rate limiting with Redis
  - Reactive programming with WebFlux
  - Spring Cloud Config Server for centralized configuration
  - Git-based configuration management
  - Environment-specific configuration profiles

### Spring WebFlux
- **What**: Reactive web framework for non-blocking applications
- **Why**: Better resource utilization, handling high concurrency
- **How**: Using reactive streams (Mono/Flux), non-blocking I/O
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 4

### Spring Security
- **What**: Framework for authentication and authorization
- **Why**: Secure applications, protect resources
- **How**: JWT tokens, OAuth2, role-based access control
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented in auth-service with JWT authentication
- **Key Learnings**:
  - JWT token generation and validation
  - Custom UserDetailsService implementation
  - Security filter chains and authentication entry points
  - Role-based authorization with @PreAuthorize
  - Password encoding with BCrypt
  - CORS configuration for cross-origin requests

### Spring Data
- **What**: Data access abstraction for various databases
- **Why**: Consistent API across different database types
- **How**: JPA for PostgreSQL, MongoDB, Neo4j, Elasticsearch
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented in user-service with JPA repositories
- **Key Learnings**:
  - Repository pattern with Spring Data JPA
  - Custom query methods and @Query annotations
  - Pagination and sorting support
  - Relationship mapping (One-to-Many, Many-to-One)
  - Index optimization for search queries

### Spring GraphQL
- **What**: GraphQL support for Spring Boot
- **Why**: Flexible API queries, reduced over-fetching
- **How**: Using Spring GraphQL starter
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented in user-service with GraphQL schema and resolvers
- **Key Learnings**:
  - GraphQL schema definition (.graphqls files)
  - Type definitions and input types
  - Query and mutation operations
  - Enum types and scalar types
  - Integration with Spring Boot applications

## 🗄️ Database Technologies

### Polyglot Persistence
- **What**: Using different database types for different data needs
- **Why**: Optimize for specific use cases, performance, scalability
- **How**: PostgreSQL for structured data, MongoDB for flexible schemas, Neo4j for graphs
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 3

### PostgreSQL
- **What**: Relational database for structured data
- **Why**: ACID compliance, complex queries, relationships
- **How**: Using for users, orders, authentication data
- **Learning Status**: ✅ Completed
- **Implementation**: Used in auth-service for user management and authentication
- **Key Learnings**:
  - Database schema design with UUID primary keys
  - JPA entity relationships (One-to-Many, Many-to-Many)
  - Repository pattern with Spring Data JPA
  - Database initialization scripts with sample data
  - Connection pooling and transaction management

### MongoDB
- **What**: NoSQL document database
- **Why**: Flexible schemas, horizontal scaling, JSON-like documents
- **How**: Using for products, inventory, flexible data
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 3

### Neo4j
- **What**: Graph database for relationship-heavy data
- **Why**: Complex relationships, graph algorithms, recommendations
- **How**: Using for product recommendations, user relationships
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 8

### Elasticsearch
- **What**: Search and analytics engine
- **Why**: Full-text search, analytics, real-time search
- **How**: Using for product search, order analytics
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 8

### Redis
- **What**: In-memory data structure store
- **Why**: Caching, session storage, real-time data
- **How**: Using for caching, rate limiting, session management
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented in API Gateway for rate limiting and caching
- **Key Learnings**:
  - Redis integration with Spring Boot
  - Rate limiting implementation with Redis
  - Reactive Redis operations
  - Connection pooling and configuration
  - Key-based rate limiting strategies

## 📡 Messaging & Communication

### Apache Kafka
- **What**: Distributed streaming platform
- **Why**: High-throughput event streaming, fault tolerance
- **How**: Using for order events, payment events, inventory updates
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 2

### RabbitMQ
- **What**: Message broker for task queues
- **Why**: Reliable message delivery, priority queues, routing
- **How**: Using for notifications, background tasks
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 2

### gRPC
- **What**: High-performance RPC framework
- **Why**: Fast inter-service communication, protocol buffers
- **How**: Using for stock checks, inventory queries
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 3

### WebSockets
- **What**: Bidirectional communication protocol
- **Why**: Real-time updates, chat functionality
- **How**: Using for live chat, real-time notifications
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 4

### Server-Sent Events (SSE)
- **What**: One-way real-time communication
- **Why**: Real-time updates, simpler than WebSockets
- **How**: Using for order status updates, notifications
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 4

## 🔐 Security & Authentication

### JWT (JSON Web Tokens)
- **What**: Stateless authentication tokens
- **Why**: Scalability, stateless authentication
- **How**: Using for API authentication, session management
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented in auth-service with access and refresh tokens
- **Key Learnings**:
  - JWT token structure and claims
  - Access token vs refresh token patterns
  - Token validation and expiration handling
  - Secure token storage and refresh mechanisms
  - JWT library usage (jjwt) with HMAC-SHA512 signing

### OAuth2
- **What**: Authorization framework
- **Why**: Secure API access, third-party integration
- **How**: Using for API authorization, client credentials
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 1

### SAML
- **What**: XML-based authentication protocol
- **Why**: Enterprise SSO, federation
- **How**: Using for enterprise authentication
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 9

### Keycloak
- **What**: Identity and access management
- **Why**: Centralized authentication, federation
- **How**: Using for advanced identity management
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 9

## 📊 Observability & Monitoring

### Prometheus
- **What**: Metrics collection and monitoring
- **Why**: Time-series metrics, alerting
- **How**: Using for service metrics, business metrics
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 5

### Grafana
- **What**: Metrics visualization and dashboards
- **Why**: Visual monitoring, alerting
- **How**: Using for metrics dashboards, alerts
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 5

### ELK Stack / Loki
- **What**: Log aggregation and analysis
- **Why**: Centralized logging, search, analysis
- **How**: Using for application logs, error tracking
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 5

### OpenTelemetry
- **What**: Distributed tracing standard
- **Why**: End-to-end request tracing, performance analysis
- **How**: Using for request tracing across services
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 5

### Jaeger
- **What**: Distributed tracing system
- **Why**: Visualize request flows, debug issues
- **How**: Using for tracing visualization
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 5

## 🐳 Containerization & Orchestration

### Docker
- **What**: Containerization platform
- **Why**: Consistent environments, isolation, portability
- **How**: Using for service containers, development environment
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented Docker Compose for development environment and Dockerfile for auth-service
- **Key Learnings**:
  - Multi-stage Docker builds for optimized images
  - Docker Compose for orchestrating multiple services
  - Health checks and service dependencies
  - Environment-specific configurations
  - Volume management for persistent data

### Docker Compose
- **What**: Multi-container Docker applications
- **Why**: Local development, service orchestration
- **How**: Using for development environment setup
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented comprehensive Docker Compose setup with all infrastructure services
- **Key Learnings**:
  - Service orchestration and networking
  - Health checks and dependency management
  - Environment variables and configuration
  - Volume mounts for data persistence
  - Service discovery and communication

### Kubernetes
- **What**: Container orchestration platform
- **Why**: Production deployment, scaling, management
- **How**: Using for production deployment
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 6

## 🧪 Testing & Quality

### JUnit 5
- **What**: Unit testing framework
- **Why**: Code quality, regression prevention
- **How**: Using for unit tests, integration tests
- **Learning Status**: 🔄 In Progress
- **Implementation**: Will implement in Phase 1

### Testcontainers
- **What**: Integration testing with real containers
- **Why**: Real database testing, integration testing
- **How**: Using for database, Kafka, Redis testing
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 2

### WireMock
- **What**: API mocking for testing
- **Why**: External API testing, controlled test environment
- **How**: Using for payment gateway, external service testing
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 3

### Gatling
- **What**: Load testing framework
- **Why**: Performance testing, scalability validation
- **How**: Using for API load testing, performance validation
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 10

## 🚀 CI/CD & DevOps

### GitHub Actions
- **What**: CI/CD automation
- **Why**: Automated testing, building, deployment
- **How**: Using for automated pipeline
- **Learning Status**: ⏳ Pending
- **Implementation**: Will implement in Phase 6

### Maven
- **What**: Build automation tool
- **Why**: Dependency management, build lifecycle
- **How**: Using for project building, dependency management
- **Learning Status**: ✅ Completed
- **Implementation**: Implemented parent POM with dependency management and service-specific POMs
- **Key Learnings**:
  - Parent-child POM relationships
  - Dependency management and version control
  - Build profiles for different environments
  - Plugin configuration and lifecycle management
  - Multi-module project structure

## 📚 Learning Resources

### Books
- "Building Microservices" by Sam Newman
- "Spring in Action" by Craig Walls
- "Event-Driven Architecture" by Hugh Blemings
- "Kafka: The Definitive Guide" by Neha Narkhede

### Online Courses
- Spring Boot Microservices on Udemy
- Apache Kafka Fundamentals
- Docker and Kubernetes Mastery

### Documentation
- Spring Boot Reference Documentation
- Apache Kafka Documentation
- Docker Documentation
- Kubernetes Documentation

## 🎯 Learning Goals by Phase

### Phase 1 Goals
- [x] Understand Spring Boot 3 fundamentals
- [x] Learn JWT authentication implementation
- [x] Master basic microservice communication
- [x] Understand Docker containerization

### Phase 2 Goals
- [ ] Learn event-driven architecture
- [ ] Understand Kafka and RabbitMQ
- [ ] Master message publishing and consuming
- [ ] Learn reliable messaging patterns

### Phase 3 Goals
- [ ] Understand polyglot persistence
- [ ] Learn gRPC implementation
- [ ] Master saga pattern for transactions
- [ ] Understand event sourcing basics

### Phase 4 Goals
- [ ] Learn reactive programming with WebFlux
- [ ] Understand WebSocket implementation
- [ ] Master Redis caching strategies
- [ ] Learn real-time communication patterns

### Phase 5 Goals
- [ ] Understand observability patterns
- [ ] Learn Prometheus metrics collection
- [ ] Master distributed tracing
- [ ] Understand centralized logging

## 📝 Notes & Observations

### Key Learnings
- *To be filled as we progress through the project*

### Challenges Faced
- *To be filled as we encounter and solve problems*

### Best Practices Discovered
- *To be filled as we learn industry best practices*

### Common Pitfalls
- *To be filled as we learn what to avoid*

---

**Last Updated**: Phase 1 - Foundation & Core Services
**Next Phase**: Phase 2 - Messaging Infrastructure
**Overall Learning Progress**: 60% Complete
