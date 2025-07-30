# Java REST API Example

This is a simple Java REST API built with Spring Boot to demonstrate basic GET endpoints.

## What is a REST API?

A REST API (Representational State Transfer) is a way for different applications to communicate over the internet using HTTP methods like GET, POST, PUT, DELETE.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/restapi/
│   │       ├── RestApiApplication.java    # Main application class
│   │       ├── controller/
│   │       │   └── UserController.java    # REST endpoints
│   │       └── model/
│   │           └── User.java              # Data model
│   └── resources/
│       └── application.properties         # Configuration
```

## Key Components Explained

### 1. `@RestController`
- Marks a class as a REST controller
- Automatically converts return values to JSON

### 2. `@RequestMapping("/api/users")`
- Defines the base URL path for all endpoints in this controller

### 3. `@GetMapping`
- Handles HTTP GET requests
- Used to retrieve data

### 4. `@PathVariable`
- Extracts values from the URL path
- Example: `/api/users/1` → `id = 1`

### 5. `@RequestParam`
- Extracts values from query parameters
- Example: `/api/users/search?name=john`

## Available Endpoints

### 1. Get All Users
- **URL**: `GET /api/users`
- **Description**: Returns all users
- **Example**: `http://localhost:8080/api/users`

### 2. Get User by ID
- **URL**: `GET /api/users/{id}`
- **Description**: Returns a specific user by ID
- **Example**: `http://localhost:8080/api/users/1`

### 3. Search Users by Name
- **URL**: `GET /api/users/search?name={name}`
- **Description**: Returns users whose names contain the search term
- **Example**: `http://localhost:8080/api/users/search?name=john`

### 4. Get User Count
- **URL**: `GET /api/users/count`
- **Description**: Returns the total number of users
- **Example**: `http://localhost:8080/api/users/count`

### 5. Health Check
- **URL**: `GET /api/users/health`
- **Description**: Simple health check endpoint
- **Example**: `http://localhost:8080/api/users/health`

## How to Run

### Prerequisites
- Java 17 or higher
- Maven

### Steps
1. **Clone or download this project**
2. **Open terminal in the project directory**
3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
4. **Access the API**:
   - Open your browser or use a tool like Postman
   - Visit: `http://localhost:8080/api/users`

## Testing the API

### Using cURL (Command Line)
```bash
# Get all users
curl http://localhost:8080/api/users

# Get user by ID
curl http://localhost:8080/api/users/1

# Search users by name
curl "http://localhost:8080/api/users/search?name=john"

# Get user count
curl http://localhost:8080/api/users/count

# Health check
curl http://localhost:8080/api/users/health
```

### Using Browser
Simply open these URLs in your browser:
- `http://localhost:8080/api/users`
- `http://localhost:8080/api/users/1`
- `http://localhost:8080/api/users/count`

## Expected Responses

### Get All Users Response
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "age": 25
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "age": 30
  }
]
```

### Get User by ID Response
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "age": 25
}
```

## Learning Points

1. **HTTP Methods**: This example focuses on GET requests
2. **URL Mapping**: How to map URLs to Java methods
3. **Path Variables**: How to extract data from URL paths
4. **Query Parameters**: How to handle search parameters
5. **JSON Response**: Automatic conversion of Java objects to JSON
6. **Error Handling**: 404 responses for not found resources

## Next Steps

After understanding this example, you can learn:
- POST requests (creating new data)
- PUT requests (updating existing data)
- DELETE requests (removing data)
- Database integration (JPA/Hibernate)
- Authentication and authorization
- Input validation
- Exception handling

## Common HTTP Status Codes

- **200 OK**: Request successful
- **404 Not Found**: Resource not found
- **400 Bad Request**: Invalid request
- **500 Internal Server Error**: Server error

This example demonstrates the foundation of REST API development in Java with Spring Boot! 