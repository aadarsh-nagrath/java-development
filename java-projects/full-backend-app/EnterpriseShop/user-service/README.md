# User Service

The User Service is a microservice responsible for managing user profiles, addresses, and preferences in the EnterpriseShop e-commerce platform.

## 🚀 Features

- **User Profile Management**: Create, read, update, and delete user profiles
- **Address Management**: Multiple addresses per user (shipping, billing, both)
- **User Preferences**: Flexible preference system with categories and key-value pairs
- **REST API**: Full CRUD operations with pagination and search
- **GraphQL API**: Flexible queries and mutations
- **Security**: Role-based access control with JWT authentication
- **Monitoring**: Health checks, metrics, and Prometheus integration

## 🏗️ Architecture

### Entities

- **User**: Core user profile information
- **Address**: User addresses with types (shipping, billing, both)
- **UserPreference**: Key-value preferences organized by categories

### Relationships

- User has many Addresses (One-to-Many)
- User has many UserPreferences (One-to-Many)
- Address belongs to User (Many-to-One)
- UserPreference belongs to User (Many-to-One)

## 🛠️ Technology Stack

- **Java 21**: Latest LTS version
- **Spring Boot 3**: Modern Spring framework
- **Spring Data JPA**: Data access layer
- **Spring Security**: Authentication and authorization
- **Spring GraphQL**: GraphQL support
- **PostgreSQL**: Primary database
- **Docker**: Containerization
- **Maven**: Build tool

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- PostgreSQL 13+
- Docker (optional)

## 🚀 Quick Start

### 1. Clone and Build

```bash
cd EnterpriseShop/user-service
mvn clean package
```

### 2. Run with Maven

```bash
mvn spring-boot:run
```

### 3. Run with Docker

```bash
docker build -t user-service .
docker run -p 8082:8082 user-service
```

### 4. Run with Docker Compose

```bash
cd EnterpriseShop
docker-compose up user-service
```

## 🔧 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | `your-secret-key-here` | JWT signing secret |
| `DB_HOST` | `localhost` | Database host |
| `DB_PORT` | `5432` | Database port |
| `DB_NAME` | `users` | Database name |
| `DB_USER` | `enterpriseshop_user` | Database username |
| `DB_PASS` | `enterpriseshop_pass` | Database password |

### Application Properties

The service uses Spring Boot's auto-configuration with the following key settings:

- **Port**: 8082
- **Context Path**: `/users`
- **Database**: PostgreSQL with connection pooling
- **JPA**: Hibernate with validation
- **Security**: JWT-based authentication
- **Monitoring**: Actuator endpoints enabled

## 📡 API Endpoints

### REST API

#### User Management

- `POST /api/users` - Create user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/auth/{authUserId}` - Get user by auth ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (soft delete)

#### User Search

- `GET /api/users` - Get all users (paginated)
- `GET /api/users/active` - Get active users
- `GET /api/users/search/name?q={name}` - Search by name
- `GET /api/users/search/city?q={city}` - Search by city
- `GET /api/users/search/country?q={country}` - Search by country

#### User Operations

- `PATCH /api/users/{id}/verification?isVerified={boolean}` - Update verification
- `PATCH /api/users/{id}/status?isActive={boolean}` - Update status
- `PATCH /api/users/{id}/last-login` - Update last login
- `GET /api/users/statistics` - Get user statistics

#### Health Check

- `GET /api/users/health` - Service health status

### GraphQL API

#### Endpoint

- `POST /graphql` - GraphQL queries and mutations
- `GET /graphiql` - GraphQL playground (development only)

#### Key Queries

```graphql
# Get user by ID
query {
  user(id: "user-uuid") {
    id
    firstName
    lastName
    email
    addresses {
      city
      country
    }
  }
}

# Search users
query {
  searchUsersByName(name: "John") {
    id
    firstName
    lastName
    email
  }
}

# Get paginated users
query {
  users(page: 0, size: 10, sortBy: "firstName", sortDirection: ASC) {
    content {
      id
      firstName
      lastName
    }
    totalElements
    totalPages
  }
}
```

#### Key Mutations

```graphql
# Create user
mutation {
  createUser(input: {
    authUserId: "auth-uuid"
    firstName: "John"
    lastName: "Doe"
    email: "john@example.com"
  }) {
    id
    firstName
    lastName
  }
}

# Update user
mutation {
  updateUser(id: "user-uuid", input: {
    firstName: "Jane"
    lastName: "Smith"
  }) {
    id
    firstName
    lastName
  }
}
```

## 🔐 Security

### Authentication

- JWT-based authentication
- Integration with Auth Service
- Stateless session management

### Authorization

- **ROLE_USER**: Basic user operations
- **ROLE_ADMIN**: Administrative operations

### Protected Endpoints

- User creation: ADMIN only
- User deletion: ADMIN only
- User statistics: ADMIN only
- Bulk operations: ADMIN only

## 📊 Monitoring

### Health Checks

- **Endpoint**: `/actuator/health`
- **Checks**: Database connectivity, service status

### Metrics

- **Endpoint**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **Custom metrics**: User counts, operation latencies

### Logging

- **Level**: Configurable (default: INFO)
- **Format**: JSON with timestamps
- **Categories**: User operations, security events, errors

## 🗄️ Database

### Schema

The service creates the following tables:

- `users` - User profiles
- `addresses` - User addresses
- `user_preferences` - User preferences

### Indexes

- Primary keys on all tables
- Email uniqueness index
- Phone uniqueness index
- Auth user ID uniqueness index
- Search indexes on name, city, country

### Initialization

Database schema is automatically created by Hibernate. Sample data can be loaded via:

```sql
-- Insert sample users
INSERT INTO users (id, auth_user_id, first_name, last_name, email, is_active, is_verified)
VALUES (gen_random_uuid(), 'auth-uuid', 'John', 'Doe', 'john@example.com', true, true);
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

### Test Coverage

```bash
mvn jacoco:report
```

## 🚀 Deployment

### Docker

```bash
# Build image
docker build -t user-service:latest .

# Run container
docker run -d \
  --name user-service \
  -p 8082:8082 \
  -e DB_HOST=postgres \
  -e DB_NAME=users \
  user-service:latest
```

### Kubernetes

```bash
# Apply manifests
kubectl apply -f k8s/

# Check deployment
kubectl get pods -l app=user-service
```

### Environment Variables

```bash
export JWT_SECRET=your-secure-secret
export DB_HOST=your-db-host
export DB_NAME=your-db-name
export DB_USER=your-db-user
export DB_PASS=your-db-pass
```

## 🔍 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Check database host and credentials
   - Verify network connectivity
   - Check firewall settings

2. **JWT Validation Failed**
   - Verify JWT_SECRET environment variable
   - Check token expiration
   - Validate token format

3. **Service Won't Start**
   - Check port availability
   - Verify Java version (requires Java 21)
   - Check application logs

### Logs

```bash
# View logs
docker logs user-service

# Follow logs
docker logs -f user-service

# Check specific errors
docker logs user-service | grep ERROR
```

### Health Check

```bash
# Check service health
curl http://localhost:8082/users/actuator/health

# Check database connectivity
curl http://localhost:8082/users/actuator/health/db
```

## 📚 API Documentation

- **REST API**: Available at `/api/users/*`
- **GraphQL**: Available at `/graphql`
- **GraphiQL**: Available at `/graphiql` (development)
- **OpenAPI**: Available at `/swagger-ui.html` (if enabled)

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

**User Service** - Part of the EnterpriseShop Microservices Platform
