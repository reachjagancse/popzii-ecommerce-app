package com.popzii.instamart.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.instamart.dto.InstamartProductRequest;
import com.popzii.instamart.dto.InstamartProductResponse;
import com.popzii.instamart.service.InstamartCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instamart/products")
public class InstamartProductController {
    private final InstamartCatalogService service;

    public InstamartProductController(InstamartCatalogService service) { this.service = service; }

    @PostMapping
    public ApiResponse<InstamartProductResponse> create(@Valid @RequestBody InstamartProductRequest request) {
        return ApiResponse.ok("Product created", service.createProduct(request));
    }

    @GetMapping
    public ApiResponse<List<InstamartProductResponse>> list(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok("Products fetched", service.listProducts(categoryId, search, page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<InstamartProductResponse> update(
        @PathVariable Long id,
        @RequestBody InstamartProductRequest request
    ) {
        return ApiResponse.ok("Product updated", service.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        service.deactivateProduct(id);
        return ApiResponse.ok("Product deactivated", null);
    }
}
