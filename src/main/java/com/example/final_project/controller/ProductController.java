package com.example.final_project.controller;

import com.example.final_project.entity.Product;
import com.example.final_project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "CRUD operations for products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // --- ЧТЕНИЕ (доступно всем ролям) ---
    @GetMapping
    @Operation(summary = "Get all products")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public List<Product> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // --- СОЗДАНИЕ (только Менеджер и Админ) ---
    @PostMapping
    @Operation(summary = "Create a new product")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public Product create(@RequestBody Product product) {
        return productService.create(product);
    }

    // --- ОБНОВЛЕНИЕ (только Менеджер и Админ) ---
    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    // --- УДАЛЕНИЕ (только Админ) ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}