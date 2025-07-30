package com.example.crud.service;

import com.example.crud.dto.ProductRequestDto;
import com.example.crud.dto.ProductResponseDto;
import com.example.crud.entity.Product;
import com.example.crud.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Product Service
 * Handles business logic for product operations
 * 
 * Demonstrates:
 * - Service layer pattern
 * - Transaction management
 * - Entity to DTO conversion
 * - Business logic implementation
 * - Error handling
 */
@Service
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Create a new product
     */
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        // Check if product with same name already exists
        if (productRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("Product with name '" + requestDto.getName() + "' already exists");
        }
        
        // Convert DTO to entity
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setQuantity(requestDto.getQuantity());
        product.setCategory(requestDto.getCategory());
        product.setSupplierEmail(requestDto.getSupplierEmail());
        
        // Save the product
        Product savedProduct = productRepository.save(product);
        
        // Convert entity to response DTO
        return convertToResponseDto(savedProduct);
    }
    
    /**
     * Get all products
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public Optional<ProductResponseDto> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::convertToResponseDto);
    }
    
    /**
     * Update product by ID
     */
    public Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto requestDto) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            
            // Check if name is being changed and if it conflicts with another product
            if (!product.getName().equals(requestDto.getName()) && 
                productRepository.existsByNameAndIdNot(requestDto.getName(), id)) {
                throw new IllegalArgumentException("Product with name '" + requestDto.getName() + "' already exists");
            }
            
            // Update product fields
            product.setName(requestDto.getName());
            product.setDescription(requestDto.getDescription());
            product.setPrice(requestDto.getPrice());
            product.setQuantity(requestDto.getQuantity());
            product.setCategory(requestDto.getCategory());
            product.setSupplierEmail(requestDto.getSupplierEmail());
            
            // Save the updated product
            Product savedProduct = productRepository.save(product);
            
            return Optional.of(convertToResponseDto(savedProduct));
        }
        
        return Optional.empty();
    }
    
    /**
     * Delete product by ID
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get products by price range
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Search products by name
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProductsByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get low stock products (quantity <= 10)
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getLowStockProducts() {
        List<Product> products = productRepository.findLowStockProducts();
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get expensive products (price > 100)
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getExpensiveProducts() {
        List<Product> products = productRepository.findExpensiveProducts();
        return products.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get product count by category
     */
    @Transactional(readOnly = true)
    public List<Object[]> getProductCountByCategory() {
        return productRepository.countProductsByCategory();
    }
    
    /**
     * Convert entity to response DTO
     */
    private ProductResponseDto convertToResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getVersion()
        );
    }
} 