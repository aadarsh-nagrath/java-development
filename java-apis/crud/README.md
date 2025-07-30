# Spring Boot CRUD Application

A comprehensive Spring Boot CRUD application demonstrating RESTful APIs, DTOs, validation, and JSON processing.

## 🎯 Learning Objectives

This application demonstrates the following concepts:

- **RESTful APIs using Spring Boot** - Complete CRUD operations with proper HTTP methods
- **DTOs for clean data transfer** - Separate request/response DTOs with validation
- **Jackson for JSON processing** - Default JSON serialization/deserialization with Spring Boot
- **Validation using javax.validation** - Comprehensive input validation with custom error messages

## 🚀 Features

### Core Features
- ✅ Complete CRUD operations (Create, Read, Update, Delete)
- ✅ RESTful API design with proper HTTP status codes
- ✅ Input validation with detailed error messages
- ✅ DTO pattern for clean data transfer
- ✅ Global exception handling
- ✅ H2 in-memory database with sample data
- ✅ Spring Data JPA with custom queries
- ✅ Transaction management

### Advanced Features
- ✅ Search and filtering capabilities
- ✅ Business logic queries (low stock, expensive products)
- ✅ Statistics and aggregation endpoints
- ✅ Health check endpoints
- ✅ CORS support for frontend integration
- ✅ Comprehensive logging
- ✅ Actuator endpoints for monitoring

## 🛠️ Technology Stack

- **Spring Boot 3.2.0** - Main framework
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database
- **Spring Validation** - Input validation
- **Jackson** - JSON processing (default with Spring Boot)
- **Maven** - Build tool
- **Java 17** - Programming language

## 📁 Project Structure

```
src/main/java/com/example/crud/
├── CrudApplication.java              # Main application class
├── controller/
│   └── ProductController.java        # REST controller
├── service/
│   └── ProductService.java           # Business logic layer
├── repository/
│   └── ProductRepository.java        # Data access layer
├── entity/
│   └── Product.java                  # JPA entity
├── dto/
│   ├── ProductRequestDto.java        # Request DTO
│   └── ProductResponseDto.java       # Response DTO
├── exception/
│   └── GlobalExceptionHandler.java   # Global exception handling
└── config/
    └── DataInitializer.java          # Sample data initialization
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone and navigate to the project**
   ```bash
   cd crud
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - H2 Console: `http://localhost:8080/h2-console`
   - Actuator Health: `http://localhost:8080/actuator/health`

### H2 Database Console
- URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Core CRUD Endpoints

#### 1. Create Product
```http
POST /api/products
Content-Type: application/json

{
  "name": "New Product",
  "description": "Product description",
  "price": 99.99,
  "quantity": 50,
  "category": "Electronics",
  "supplierEmail": "supplier@example.com"
}
```

#### 2. Get All Products
```http
GET /api/products
```

#### 3. Get Product by ID
```http
GET /api/products/{id}
```

#### 4. Update Product
```http
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Updated Product",
  "description": "Updated description",
  "price": 149.99,
  "quantity": 75,
  "category": "Electronics",
  "supplierEmail": "supplier@example.com"
}
```

#### 5. Delete Product
```http
DELETE /api/products/{id}
```

### Search and Filter Endpoints

#### 6. Search Products by Name
```http
GET /api/products/search?name=laptop
```

#### 7. Get Products by Category
```http
GET /api/products/category/Electronics
```

#### 8. Get Products by Price Range
```http
GET /api/products/price-range?minPrice=50&maxPrice=200
```

#### 9. Get Low Stock Products
```http
GET /api/products/low-stock
```

#### 10. Get Expensive Products
```http
GET /api/products/expensive
```

#### 11. Get Product Statistics
```http
GET /api/products/stats/category-count
```

#### 12. Health Check
```http
GET /api/products/health
```

## 🔍 Validation Examples

### Request Validation
The application uses comprehensive validation:

```java
@NotBlank(message = "Product name is required")
@Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
private String name;

@NotNull(message = "Price is required")
@DecimalMin(value = "0.01", message = "Price must be greater than 0")
@DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
private BigDecimal price;

@Email(message = "Please provide a valid email address")
private String supplierEmail;
```

### Error Response Example
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation failed",
  "message": "One or more fields failed validation",
  "details": {
    "name": "Product name is required",
    "price": "Price must be greater than 0"
  }
}
```

## 🎯 Key Learning Concepts

### 1. RESTful APIs
- **GET** for retrieving data
- **POST** for creating new resources
- **PUT** for full updates
- **DELETE** for removing resources
- Proper HTTP status codes (200, 201, 400, 404, 500)

### 2. DTOs (Data Transfer Objects)
- **ProductRequestDto**: For incoming requests with validation
- **ProductResponseDto**: For outgoing responses (excludes sensitive data)
- Clean separation between API layer and database entities

### 3. Jackson JSON Processing
- Automatic JSON serialization/deserialization
- Custom annotations for formatting (`@JsonFormat`, `@JsonInclude`)
- Null value handling
- Date/time formatting

### 4. Validation
- **@Valid** annotation for request validation
- **@NotNull**, **@NotBlank**, **@Size** for field validation
- **@DecimalMin**, **@DecimalMax** for numeric validation
- **@Email** for email validation
- Custom error messages
- Global exception handling

### 5. Spring Boot Features
- **Spring Data JPA**: Repository pattern with custom queries
- **Transaction Management**: `@Transactional` annotations
- **Actuator**: Health checks and monitoring
- **DevTools**: Hot reload for development
- **H2 Console**: Database management interface

## 🧪 Testing the API

### Using curl

#### Create a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "A test product",
    "price": 29.99,
    "quantity": 100,
    "category": "Test",
    "supplierEmail": "test@example.com"
  }'
```

#### Get All Products
```bash
curl http://localhost:8080/api/products
```

#### Get Product by ID
```bash
curl http://localhost:8080/api/products/1
```

#### Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product",
    "description": "Updated description",
    "price": 39.99,
    "quantity": 75,
    "category": "Updated",
    "supplierEmail": "updated@example.com"
  }'
```

#### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### Using Postman
Import the following collection or create requests manually:

1. **Create Product** (POST)
2. **Get All Products** (GET)
3. **Get Product by ID** (GET)
4. **Update Product** (PUT)
5. **Delete Product** (DELETE)
6. **Search Products** (GET with query params)

## 📊 Sample Data

The application comes pre-loaded with sample products:

- MacBook Pro (Electronics)
- iPhone 15 (Electronics)
- Spring Boot in Action (Books)
- Premium Coffee Beans (Food & Beverages)
- Ergonomic Office Chair (Furniture)
- Smart Watch (Electronics)
- Wireless Headphones (Electronics)
- Premium Notebook (Office Supplies)
- Stainless Steel Water Bottle (Sports & Outdoors)
- Indoor Plant (Home & Garden)

## 🔧 Configuration

### Application Properties
Key configurations in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  jackson:
    default-property-inclusion: non_null
server:
  port: 8080
  servlet:
    context-path: /api
```

## 🚀 Next Steps

To extend this application, consider adding:

1. **Authentication & Authorization** - Spring Security
2. **Pagination** - Pageable requests
3. **Sorting** - Sortable results
4. **File Upload** - Product images
5. **Caching** - Redis or EhCache
6. **API Documentation** - Swagger/OpenAPI
7. **Testing** - Unit and integration tests
8. **Docker** - Containerization
9. **CI/CD** - GitHub Actions
10. **Monitoring** - Prometheus, Grafana

## 📝 License

This project is for educational purposes. Feel free to use and modify as needed.

---

**Happy Learning! 🎉** 