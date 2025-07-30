package com.example.crud.config;

import com.example.crud.entity.Product;
import com.example.crud.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Data Initializer
 * Populates the database with sample data on application startup
 * 
 * Demonstrates:
 * - CommandLineRunner for initialization
 * - Sample data creation
 * - Database seeding
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no products exist
        if (productRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        // Create sample products
        Product laptop = new Product();
        laptop.setName("MacBook Pro");
        laptop.setDescription("High-performance laptop for professionals");
        laptop.setPrice(new BigDecimal("1299.99"));
        laptop.setQuantity(50);
        laptop.setCategory("Electronics");
        laptop.setSupplierEmail("apple@example.com");
        productRepository.save(laptop);
        
        Product phone = new Product();
        phone.setName("iPhone 15");
        phone.setDescription("Latest smartphone with advanced features");
        phone.setPrice(new BigDecimal("999.99"));
        phone.setQuantity(100);
        phone.setCategory("Electronics");
        phone.setSupplierEmail("apple@example.com");
        productRepository.save(phone);
        
        Product book = new Product();
        book.setName("Spring Boot in Action");
        book.setDescription("Comprehensive guide to Spring Boot development");
        book.setPrice(new BigDecimal("49.99"));
        book.setQuantity(25);
        book.setCategory("Books");
        book.setSupplierEmail("manning@example.com");
        productRepository.save(book);
        
        Product coffee = new Product();
        coffee.setName("Premium Coffee Beans");
        coffee.setDescription("Organic coffee beans from Colombia");
        coffee.setPrice(new BigDecimal("15.99"));
        coffee.setQuantity(200);
        coffee.setCategory("Food & Beverages");
        coffee.setSupplierEmail("coffee@example.com");
        productRepository.save(coffee);
        
        Product chair = new Product();
        chair.setName("Ergonomic Office Chair");
        chair.setDescription("Comfortable chair for long working hours");
        chair.setPrice(new BigDecimal("299.99"));
        chair.setQuantity(15);
        chair.setCategory("Furniture");
        chair.setSupplierEmail("furniture@example.com");
        productRepository.save(chair);
        
        Product watch = new Product();
        watch.setName("Smart Watch");
        watch.setDescription("Fitness tracking and notifications");
        watch.setPrice(new BigDecimal("199.99"));
        watch.setQuantity(75);
        watch.setCategory("Electronics");
        watch.setSupplierEmail("tech@example.com");
        productRepository.save(watch);
        
        Product headphones = new Product();
        headphones.setName("Wireless Headphones");
        headphones.setDescription("Noise-cancelling wireless headphones");
        headphones.setPrice(new BigDecimal("149.99"));
        headphones.setQuantity(30);
        headphones.setCategory("Electronics");
        headphones.setSupplierEmail("audio@example.com");
        productRepository.save(headphones);
        
        Product notebook = new Product();
        notebook.setName("Premium Notebook");
        notebook.setDescription("High-quality paper notebook");
        notebook.setPrice(new BigDecimal("12.99"));
        notebook.setQuantity(150);
        notebook.setCategory("Office Supplies");
        notebook.setSupplierEmail("office@example.com");
        productRepository.save(notebook);
        
        Product waterBottle = new Product();
        waterBottle.setName("Stainless Steel Water Bottle");
        waterBottle.setDescription("Insulated water bottle for daily use");
        waterBottle.setPrice(new BigDecimal("24.99"));
        waterBottle.setQuantity(80);
        waterBottle.setCategory("Sports & Outdoors");
        waterBottle.setSupplierEmail("outdoor@example.com");
        productRepository.save(waterBottle);
        
        Product plant = new Product();
        plant.setName("Indoor Plant");
        plant.setDescription("Low-maintenance indoor plant");
        plant.setPrice(new BigDecimal("29.99"));
        plant.setQuantity(20);
        plant.setCategory("Home & Garden");
        plant.setSupplierEmail("garden@example.com");
        productRepository.save(plant);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Total products created: " + productRepository.count());
    }
} 