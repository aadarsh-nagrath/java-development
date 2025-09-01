# API Gateway

The API Gateway is the front door to the EnterpriseShop microservices platform. It handles request routing, authentication, rate limiting, and provides a unified entry point for all client applications.

## 🚀 Features

- **Request Routing**: Routes requests to appropriate microservices
- **Load Balancing**: Distributes traffic across service instances
- **Authentication**: JWT token validation and security
- **Rate Limiting**: Redis-based rate limiting per service and user
- **Circuit Breaker**: Resilience4j circuit breaker patterns
- **CORS Support**: Cross-origin resource sharing configuration
- **Health Monitoring**: Comprehensive health checks and metrics
- **Service Discovery**: Integration with Eureka service registry

## 🏗️ Architecture

### Components

- **Gateway Routes**: Configured routes for each microservice
- **Security Filters**: JWT validation and authentication
- **Rate Limiters**: Per-service and per-user rate limiting
- **Circuit Breakers**: Fault tolerance for service failures
- **Fallback Handlers**: Graceful degradation when services are down

### Request Flow

```
Client Request → API Gateway → JWT Validation → Rate Limiting → Circuit Breaker → Service
                ↓
            Fallback (if service down)
```

## 🛠️ Technology Stack

- **Spring Cloud Gateway**: Reactive API gateway
- **Spring Security**: Authentication and authorization
- **Spring Cloud Netflix**: Service discovery with Eureka
- **Redis**: Rate limiting and caching
- **Resilience4j**: Circuit breaker implementation
- **Micrometer**: Metrics and monitoring
- **Docker**: Containerization

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- Redis 6+
- Eureka Server (for service discovery)
- Docker (optional)

## 🚀 Quick Start

### 1. Clone and Build

```bash
cd EnterpriseShop/api-gateway
mvn clean package
```

### 2. Run with Maven

```bash
mvn spring-boot:run
```

### 3. Run with Docker

```bash
docker build -t api-gateway .
docker run -p 8080:8080 api-gateway
```

### 4. Run with Docker Compose

```bash
cd EnterpriseShop
docker-compose up api-gateway
```

## 🔧 Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JWT_SECRET` | `your-secret-key-here` | JWT signing secret |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `EUREKA_URL` | `http://localhost:8761/eureka/` | Eureka server URL |

### Application Properties

The gateway uses Spring Cloud Gateway configuration with:

- **Port**: 8080
- **Service Discovery**: Eureka client enabled
- **Rate Limiting**: Redis-based with configurable limits
- **Circuit Breakers**: Resilience4j with configurable thresholds
- **Security**: JWT-based authentication

## 📡 API Endpoints

### Gateway Routes

#### Auth Service
- **Base Path**: `/auth/**`
- **Rate Limit**: 5 req/sec, burst of 10
- **Circuit Breaker**: 50% failure threshold

#### User Service
- **Base Path**: `/users/**`
- **Rate Limit**: 20 req/sec, burst of 40
- **Circuit Breaker**: 50% failure threshold

#### Health Checks
- **Base Path**: `/health/**`
- **Public Access**: No authentication required

#### API Documentation
- **Base Path**: `/docs/**`
- **Public Access**: No authentication required

### Health Endpoints

#### Basic Health Check
```bash
GET /health
```

#### Detailed Health Check
```bash
GET /health/detailed
```

#### Service Health
```bash
GET /health/services
```

#### Circuit Breaker Status
```bash
GET /health/circuit-breakers
```

#### Rate Limiter Status
```bash
GET /health/rate-limiters
```

### Fallback Endpoints

When services are unavailable, the gateway provides fallback responses:

- `/fallback/auth` - Auth service fallback
- `/fallback/user` - User service fallback
- `/fallback/health` - Health check fallback
- `/fallback/docs` - Documentation fallback

## 🔐 Security

### Authentication

- JWT token validation for all protected routes
- Token extraction from Authorization header
- Role-based access control
- User information passed to downstream services

### Public Endpoints

- `/health/**` - Health checks
- `/actuator/health` - Actuator health
- `/actuator/info` - Actuator info
- `/auth/api/auth/**` - Authentication endpoints
- `/fallback/**` - Fallback responses

### Protected Endpoints

- `/users/**` - User service endpoints
- `/auth/**` - Other auth service endpoints
- All other service endpoints

## 📊 Monitoring

### Actuator Endpoints

- **Health**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **Gateway**: `/actuator/gateway`

### Metrics

- Request counts and response times
- Circuit breaker states
- Rate limiter usage
- Service discovery status

### Health Checks

- Gateway service health
- Redis connectivity
- Eureka service discovery
- Downstream service status

## 🗄️ Rate Limiting

### Configuration

The gateway implements Redis-based rate limiting with different policies per service:

#### Auth Service
- **Rate**: 5 requests per second
- **Burst**: 10 requests
- **Key**: User ID or IP address

#### User Service
- **Rate**: 20 requests per second
- **Burst**: 40 requests
- **Key**: User ID or IP address

#### Default
- **Rate**: 10 requests per second
- **Burst**: 20 requests
- **Key**: IP address

### Key Resolution

- **User-based**: When JWT token is present
- **IP-based**: For anonymous requests
- **Service-based**: Per-service limits

## ⚡ Circuit Breakers

### Configuration

Circuit breakers are configured using Resilience4j:

- **Sliding Window**: 10 requests
- **Minimum Calls**: 5 requests
- **Failure Threshold**: 50%
- **Wait Duration**: 30 seconds

### States

- **CLOSED**: Normal operation
- **OPEN**: Service unavailable, fallback active
- **HALF_OPEN**: Testing service recovery

## 🔄 Service Discovery

### Eureka Integration

- Automatic service registration
- Service instance discovery
- Load balancing across instances
- Health check integration

### Service Locator

- Dynamic route resolution
- Service instance selection
- Failover handling

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
# Test health endpoint
curl http://localhost:8080/health

# Test auth service routing (requires JWT)
curl -H "Authorization: Bearer <jwt>" \
     http://localhost:8080/auth/api/auth/me

# Test user service routing (requires JWT)
curl -H "Authorization: Bearer <jwt>" \
     http://localhost:8080/users/api/users
```

## 🚀 Deployment

### Docker

```bash
# Build image
docker build -t api-gateway:latest .

# Run container
docker run -d \
  --name api-gateway \
  -p 8080:8080 \
  -e REDIS_HOST=redis \
  -e EUREKA_URL=http://eureka:8761/eureka/ \
  api-gateway:latest
```

### Kubernetes

```bash
# Apply manifests
kubectl apply -f k8s/

# Check deployment
kubectl get pods -l app=api-gateway
```

### Environment Variables

```bash
export JWT_SECRET=your-secure-secret
export REDIS_HOST=your-redis-host
export EUREKA_URL=http://your-eureka:8761/eureka/
```

## 🔍 Troubleshooting

### Common Issues

1. **Service Discovery Failed**
   - Check Eureka server connectivity
   - Verify service registration
   - Check network configuration

2. **Rate Limiting Not Working**
   - Verify Redis connectivity
   - Check Redis configuration
   - Monitor rate limiter metrics

3. **Circuit Breaker Always Open**
   - Check downstream service health
   - Verify service endpoints
   - Monitor failure rates

4. **Authentication Failed**
   - Verify JWT secret configuration
   - Check token format
   - Validate token expiration

### Logs

```bash
# View logs
docker logs api-gateway

# Follow logs
docker logs -f api-gateway

# Check specific errors
docker logs api-gateway | grep ERROR
```

### Health Check

```bash
# Check gateway health
curl http://localhost:8080/health

# Check detailed health
curl http://localhost:8080/health/detailed

# Check service discovery
curl http://localhost:8080/health/services
```

## 📚 API Documentation

- **Gateway Routes**: Available at `/actuator/gateway`
- **Service Health**: Available at `/health/services`
- **Circuit Breakers**: Available at `/health/circuit-breakers`
- **Rate Limiters**: Available at `/health/rate-limiters`

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

**API Gateway** - Part of the EnterpriseShop Microservices Platform
