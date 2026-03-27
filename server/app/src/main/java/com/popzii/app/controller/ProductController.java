package com.popzii.app.controller;

import org.springframework.web.bind.annotation.*;
import com.popzii.product.repository.ProductRepository;
import com.popzii.product.model.Product;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository repo;

    public ProductController(ProductRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Product> list() { return repo.findAll(); }

    @PostMapping
    public Product create(@RequestBody Product p) { return repo.save(p); }
}
