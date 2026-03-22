package com.twicenice.twicenice_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:4200", "https://twicenice.netlify.app"})
public class ProductController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<?> getAllPublicProducts() {
        try {
            List<Map<String, Object>> products = jdbcTemplate.queryForList(
                "SELECT id, name, description, price, image_url, category, stock FROM product"
            );
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "DB Error: " + e.getClass().getName() + " - " + e.getMessage()
            );
        }
    }
}
