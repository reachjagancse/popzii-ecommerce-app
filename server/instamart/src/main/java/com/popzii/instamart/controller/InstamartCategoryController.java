package com.popzii.instamart.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.instamart.dto.InstamartCategoryRequest;
import com.popzii.instamart.dto.InstamartCategoryResponse;
import com.popzii.instamart.service.InstamartCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instamart/categories")
public class InstamartCategoryController {
    private final InstamartCatalogService service;

    public InstamartCategoryController(InstamartCatalogService service) { this.service = service; }

    @PostMapping
    public ApiResponse<InstamartCategoryResponse> create(@Valid @RequestBody InstamartCategoryRequest request) {
        return ApiResponse.ok("Category created", service.createCategory(request));
    }

    @GetMapping
    public ApiResponse<List<InstamartCategoryResponse>> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok("Categories fetched", service.listCategories(page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<InstamartCategoryResponse> update(
        @PathVariable Long id,
        @RequestBody InstamartCategoryRequest request
    ) {
        return ApiResponse.ok("Category updated", service.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        service.deactivateCategory(id);
        return ApiResponse.ok("Category deactivated", null);
    }
}
