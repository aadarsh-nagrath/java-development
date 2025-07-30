package com.example.crud.repository;

import com.example.crud.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Product Repository
 * Spring Data JPA repository for Product entity
 * 
 * Demonstrates:
 * - Spring Data JPA repository pattern
 * - Custom query methods
 * - Method name-based queries
 * - @Query annotations for complex queries
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find products by category
     * Method name-based query
     */
    List<Product> findByCategory(String category);
    
    /**
     * Find products by category ignoring case
     */
    List<Product> findByCategoryIgnoreCase(String category);
    
    /**
     * Find products with price less than specified value
     */
    List<Product> findByPriceLessThan(BigDecimal price);
    
    /**
     * Find products with price between min and max values
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products with quantity greater than specified value
     */
    List<Product> findByQuantityGreaterThan(Integer quantity);
    
    /**
     * Find products by name containing the specified string (case-insensitive)
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products by description containing the specified string (case-insensitive)
     */
    List<Product> findByDescriptionContainingIgnoreCase(String description);
    
    /**
     * Find products by supplier email
     */
    Optional<Product> findBySupplierEmail(String supplierEmail);
    
    /**
     * Custom query to find products with low stock (quantity <= 10)
     */
    @Query("SELECT p FROM Product p WHERE p.quantity <= 10")
    List<Product> findLowStockProducts();
    
    /**
     * Custom query to find products by category and price range
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByCategoryAndPriceRange(@Param("category") String category,
                                           @Param("minPrice") BigDecimal minPrice,
                                           @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Custom query to find expensive products (price > 100)
     */
    @Query("SELECT p FROM Product p WHERE p.price > 100 ORDER BY p.price DESC")
    List<Product> findExpensiveProducts();
    
    /**
     * Custom query to count products by category
     */
    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> countProductsByCategory();
    
    /**
     * Check if product exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Check if product exists by name and id is not equal to the given id (for updates)
     */
    boolean existsByNameAndIdNot(String name, Long id);
} 