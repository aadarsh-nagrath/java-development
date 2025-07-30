package com.example.crud.controller;

import com.example.crud.dto.ProductRequestDto;
import com.example.crud.dto.ProductResponseDto;
import com.example.crud.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Product REST Controller
 * Handles HTTP requests for product operations
 * 
 * Demonstrates:
 * - RESTful API design
 * - CRUD operations (Create, Read, Update, Delete)
 * - Input validation with @Valid
 * - Proper HTTP status codes
 * - Request/Response DTOs
 * - Exception handling
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * POST /api/products
     * Create a new product
     * 
     * Demonstrates:
     * - @Valid for input validation
     * - DTO pattern for request/response
     * - Proper HTTP status codes
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        try {
            ProductResponseDto createdProduct = productService.createProduct(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for validation errors
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/products
     * Get all products
     * 
     * Demonstrates:
     * - GET request for retrieving data
     * - List response
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/{id}
     * Get product by ID
     * 
     * Demonstrates:
     * - Path variable usage
     * - Optional response handling
     * - 404 Not Found for missing resources
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        Optional<ProductResponseDto> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * PUT /api/products/{id}
     * Update product by ID
     * 
     * Demonstrates:
     * - PUT for full updates
     * - @Valid for input validation
     * - 404 Not Found for missing resources
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, 
                                                         @Valid @RequestBody ProductRequestDto requestDto) {
        try {
            Optional<ProductResponseDto> updatedProduct = productService.updateProduct(id, requestDto);
            return updatedProduct.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request for validation errors
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * DELETE /api/products/{id}
     * Delete product by ID
     * 
     * Demonstrates:
     * - DELETE request
     * - Boolean response for operation success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    /**
     * GET /api/products/category/{category}
     * Get products by category
     * 
     * Demonstrates:
     * - Query by category
     * - Path variable for filtering
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponseDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/search
     * Search products by name
     * 
     * Demonstrates:
     * - Query parameter usage
     * - Search functionality
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByName(@RequestParam String name) {
        List<ProductResponseDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/price-range
     * Get products by price range
     * 
     * Demonstrates:
     * - Multiple query parameters
     * - Range queries
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<ProductResponseDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/low-stock
     * Get products with low stock (quantity <= 10)
     * 
     * Demonstrates:
     * - Business logic queries
     * - Specialized endpoints
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts() {
        List<ProductResponseDto> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/expensive
     * Get expensive products (price > 100)
     * 
     * Demonstrates:
     * - Business logic queries
     * - Specialized endpoints
     */
    @GetMapping("/expensive")
    public ResponseEntity<List<ProductResponseDto>> getExpensiveProducts() {
        List<ProductResponseDto> products = productService.getExpensiveProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * GET /api/products/stats/category-count
     * Get product count by category
     * 
     * Demonstrates:
     * - Aggregation queries
     * - Statistics endpoints
     */
    @GetMapping("/stats/category-count")
    public ResponseEntity<List<Object[]>> getProductCountByCategory() {
        List<Object[]> categoryStats = productService.getProductCountByCategory();
        return ResponseEntity.ok(categoryStats);
    }
    
    /**
     * GET /api/products/health
     * Health check endpoint
     * 
     * Demonstrates:
     * - Health check pattern
     * - Simple status endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Product API is running!");
    }
} 