# Auth Service

The Authentication Service is a Spring Boot microservice that handles user authentication, authorization, and JWT token management for the EnterpriseShop platform.

## Features

- **User Registration**: Register new users with validation
- **User Authentication**: Login with username/email and password
- **JWT Token Management**: Generate and validate JWT access tokens
- **Refresh Token Support**: Secure token refresh mechanism
- **Role-Based Authorization**: Support for different user roles (USER, ADMIN, MODERATOR)
- **Password Security**: BCrypt password hashing
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **Health Monitoring**: Actuator endpoints for health checks
- **Metrics**: Prometheus metrics integration

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.2.0
- **Spring Security**: JWT-based authentication
- **Spring Data JPA**: Database operations
- **PostgreSQL**: Primary database
- **Maven**: Build tool
- **Docker**: Containerization

## Prerequisites

- Java 21
- Maven 3.8+
- PostgreSQL 15+
- Docker (optional)

## Quick Start

### 1. Database Setup

Make sure PostgreSQL is running and the database is initialized:

```bash
# Start PostgreSQL (if using Docker)
docker run -d \
  --name postgres \
  -e POSTGRES_DB=enterpriseshop \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine

# Run the database initialization script
psql -h localhost -U postgres -d enterpriseshop -f ../scripts/init-db.sql
```

### 2. Build and Run

```bash
# Build the project
mvn clean package

# Run the application
java -jar target/auth-service-1.0.0-SNAPSHOT.jar
```

### 3. Using Docker

```bash
# Build Docker image
docker build -t enterpriseshop-auth-service .

# Run container
docker run -d \
  --name auth-service \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/auth_service_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  enterpriseshop-auth-service
```

## API Endpoints

### Authentication

- `POST /auth/api/auth/register` - Register a new user
- `POST /auth/api/auth/login` - Authenticate user
- `POST /auth/api/auth/refresh` - Refresh access token
- `POST /auth/api/auth/logout` - Logout user
- `GET /auth/api/auth/me` - Get current user info

### Health & Monitoring

- `GET /auth/actuator/health` - Health check
- `GET /auth/actuator/info` - Application info
- `GET /auth/actuator/prometheus` - Prometheus metrics

## API Examples

### Register User

```bash
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
```

### Login

```bash
curl -X POST http://localhost:8081/auth/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "password123"
  }'
```

### Get Current User

```bash
curl -X GET http://localhost:8081/auth/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Refresh Token

```bash
curl -X POST "http://localhost:8081/auth/api/auth/refresh?refreshToken=YOUR_REFRESH_TOKEN"
```

## Configuration

### Environment Variables

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_SECURITY_JWT_SECRET`: JWT signing secret
- `SPRING_SECURITY_JWT_EXPIRATION`: JWT expiration time (ms)
- `SPRING_SECURITY_JWT_REFRESH_EXPIRATION`: Refresh token expiration time (ms)

### Application Properties

Key configuration properties in `application.yml`:

```yaml
spring:
  application:
    name: auth-service
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_service_db
    username: postgres
    password: postgres
  security:
    jwt:
      secret: your-jwt-secret-key
      expiration: 86400000  # 24 hours
      refresh-expiration: 604800000  # 7 days

server:
  port: 8081
  servlet:
    context-path: /auth
```

## Database Schema

### Users Table
- `id` (UUID, Primary Key)
- `username` (VARCHAR, Unique)
- `email` (VARCHAR, Unique)
- `password_hash` (VARCHAR)
- `first_name` (VARCHAR)
- `last_name` (VARCHAR)
- `is_active` (BOOLEAN)
- `is_verified` (BOOLEAN)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Roles Table
- `id` (UUID, Primary Key)
- `name` (VARCHAR, Unique)
- `description` (TEXT)
- `created_at` (TIMESTAMP)

### User Roles Table (Many-to-Many)
- `user_id` (UUID, Foreign Key)
- `role_id` (UUID, Foreign Key)

### Refresh Tokens Table
- `id` (UUID, Primary Key)
- `user_id` (UUID, Foreign Key)
- `token` (VARCHAR)
- `expires_at` (TIMESTAMP)
- `created_at` (TIMESTAMP)

## Security

### JWT Token Structure

Access tokens contain:
- User ID
- Username
- Email
- Roles
- Issued at
- Expiration time

Refresh tokens contain:
- User ID
- Username
- Token type ("refresh")
- Issued at
- Expiration time

### Password Security

- Passwords are hashed using BCrypt
- Default cost factor: 10
- Salt is automatically generated

### CORS Configuration

CORS is enabled for all origins in development. For production, configure specific origins.

## Monitoring

### Health Checks

The service provides health check endpoints:
- Database connectivity
- Application status
- Custom health indicators

### Metrics

Prometheus metrics are exposed at `/actuator/prometheus`:
- HTTP request metrics
- JVM metrics
- Database connection metrics

### Logging

Structured logging with different levels:
- DEBUG: Detailed application logs
- INFO: General application events
- WARN: Warning messages
- ERROR: Error messages

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthServiceTest

# Run with coverage
mvn jacoco:report
```

### Code Quality

```bash
# Check code style
mvn checkstyle:check

# Run SonarQube analysis
mvn sonar:sonar
```

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify PostgreSQL is running
   - Check database credentials
   - Ensure database exists

2. **JWT Token Issues**
   - Verify JWT secret is configured
   - Check token expiration settings
   - Ensure proper token format

3. **Port Already in Use**
   - Change port in `application.yml`
   - Kill process using port 8081

### Logs

Check application logs for detailed error information:
```bash
# View logs
tail -f logs/auth-service.log

# Search for errors
grep ERROR logs/auth-service.log
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is part of the EnterpriseShop platform.
