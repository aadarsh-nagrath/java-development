# API Testing Guide

This guide demonstrates how to test all the features of the Spring Boot CRUD application.

## 🚀 Quick Start

The application is running at: `http://localhost:8080/api`

## 📋 Test Commands

### 1. Health Check
```bash
curl http://localhost:8080/api/products/health
```

### 2. Get All Products
```bash
curl http://localhost:8080/api/products
```

### 3. Get Product by ID
```bash
curl http://localhost:8080/api/products/1
```

### 4. Create a New Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Product",
    "description": "A new product for testing",
    "price": 99.99,
    "quantity": 50,
    "category": "Test",
    "supplierEmail": "test@example.com"
  }'
```

### 5. Update a Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Product",
    "description": "Updated description",
    "price": 149.99,
    "quantity": 75,
    "category": "Updated",
    "supplierEmail": "updated@example.com"
  }'
```

### 6. Delete a Product
```bash
curl -X DELETE http://localhost:8080/api/products/11
```

### 7. Search Products by Name
```bash
curl "http://localhost:8080/api/products/search?name=laptop"
```

### 8. Get Products by Category
```bash
curl http://localhost:8080/api/products/category/Electronics
```

### 9. Get Products by Price Range
```bash
curl "http://localhost:8080/api/products/price-range?minPrice=50&maxPrice=200"
```

### 10. Get Low Stock Products
```bash
curl http://localhost:8080/api/products/low-stock
```

### 11. Get Expensive Products
```bash
curl http://localhost:8080/api/products/expensive
```

### 12. Get Product Statistics
```bash
curl http://localhost:8080/api/products/stats/category-count
```

## 🔍 Validation Testing

### Test Invalid Data (Should Return 400 Bad Request)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "price": -10,
    "quantity": -5
  }'
```

Expected Response:
```json
{
  "timestamp": "2025-07-30T17:51:41.204985",
  "status": 400,
  "error": "Validation failed",
  "message": "One or more fields failed validation",
  "details": {
    "quantity": "Quantity cannot be negative",
    "price": "Price must be greater than 0",
    "name": "Product name must be between 2 and 100 characters",
    "description": "Product description is required",
    "category": "Category is required"
  }
}
```

## 🎯 Features Demonstrated

### ✅ RESTful APIs
- **GET** `/api/products` - Retrieve all products
- **GET** `/api/products/{id}` - Retrieve specific product
- **POST** `/api/products` - Create new product
- **PUT** `/api/products/{id}` - Update existing product
- **DELETE** `/api/products/{id}` - Delete product

### ✅ DTOs for Clean Data Transfer
- **ProductRequestDto** - For incoming requests with validation
- **ProductResponseDto** - For outgoing responses (excludes sensitive data)

### ✅ Jackson JSON Processing
- Automatic JSON serialization/deserialization
- Custom date formatting (`@JsonFormat`)
- Null value handling (`@JsonInclude`)

### ✅ Validation using javax.validation
- `@NotBlank` - Required string fields
- `@Size` - String length validation
- `@NotNull` - Required fields
- `@DecimalMin/@DecimalMax` - Numeric range validation
- `@Email` - Email format validation
- `@Valid` - Request validation

## 🗄️ Database Access

### H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### Sample Data
The application comes pre-loaded with 10 sample products:
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

## 🔧 Monitoring

### Actuator Endpoints
- Health Check: `http://localhost:8080/actuator/health`
- Application Info: `http://localhost:8080/actuator/info`
- Metrics: `http://localhost:8080/actuator/metrics`

## 📊 Expected Results

### Sample Product Response
```json
{
  "id": 1,
  "name": "MacBook Pro",
  "description": "High-performance laptop for professionals",
  "price": 1299.99,
  "quantity": 50,
  "category": "Electronics",
  "createdAt": "2025-07-30 17:50:52",
  "version": 0
}
```

### Electronics Category (4 products)
- MacBook Pro
- iPhone 15
- Smart Watch
- Wireless Headphones

### Expensive Products (>$100)
- MacBook Pro ($1299.99)
- iPhone 15 ($999.99)
- Ergonomic Office Chair ($299.99)
- Smart Watch ($199.99)
- Wireless Headphones ($149.99)

## 🎉 Success Indicators

✅ Application starts without errors  
✅ All CRUD operations work correctly  
✅ Validation returns proper error messages  
✅ JSON responses are properly formatted  
✅ Search and filter endpoints work  
✅ Business logic queries return expected results  
✅ H2 console is accessible  
✅ Actuator endpoints are available  

## 🚀 Next Steps

1. **Test with Postman** - Import the endpoints into Postman for easier testing
2. **Add Authentication** - Implement Spring Security
3. **Add Pagination** - Implement Pageable requests
4. **Add File Upload** - Implement product image upload
5. **Add Caching** - Implement Redis caching
6. **Add API Documentation** - Implement Swagger/OpenAPI
7. **Add Unit Tests** - Write comprehensive test coverage
8. **Add Integration Tests** - Test full request/response cycles

---

**Happy Testing! 🎯** 