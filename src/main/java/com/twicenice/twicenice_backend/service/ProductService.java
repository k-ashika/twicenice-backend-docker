package com.twicenice.twicenice_backend.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twicenice.twicenice_backend.model.Product;
import com.twicenice.twicenice_backend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
    try {
        System.out.println("=== FETCHING ALL PRODUCTS ===");
        List<Product> products = productRepository.findAll();
        System.out.println("=== FOUND " + products.size() + " PRODUCTS ===");
        for (Product p : products) {
            System.out.println("Product: " + p.getId() + " | " + p.getName() + " | " + p.getImageUrl());
        }
        return products;
    } catch (Exception e) {
        System.err.println("=== ERROR IN getAllProducts ===");
        System.err.println("Error type: " + e.getClass().getName());
        System.err.println("Error message: " + e.getMessage());
        if (e.getCause() != null) {
            System.err.println("Caused by: " + e.getCause().getClass().getName());
            System.err.println("Cause message: " + e.getCause().getMessage());
        }
        throw e;
    }
}
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id).orElseThrow();
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setCategory(updatedProduct.getCategory());
        existing.setStock(updatedProduct.getStock());
        if (updatedProduct.getImageUrl() != null && !updatedProduct.getImageUrl().isBlank()) {
            existing.setImageUrl(updatedProduct.getImageUrl());
        }
        return productRepository.save(existing);
    }
}
