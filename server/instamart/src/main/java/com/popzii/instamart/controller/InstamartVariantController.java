package com.popzii.instamart.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.instamart.dto.InstamartVariantRequest;
import com.popzii.instamart.dto.InstamartVariantResponse;
import com.popzii.instamart.service.InstamartCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instamart/variants")
public class InstamartVariantController {
    private final InstamartCatalogService service;

    public InstamartVariantController(InstamartCatalogService service) { this.service = service; }

    @PostMapping
    public ApiResponse<InstamartVariantResponse> create(@Valid @RequestBody InstamartVariantRequest request) {
        return ApiResponse.ok("Variant created", service.createVariant(request));
    }

    @GetMapping
    public ApiResponse<List<InstamartVariantResponse>> list(
        @RequestParam(required = false) Long productId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok("Variants fetched", service.listVariants(productId, page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<InstamartVariantResponse> update(
        @PathVariable Long id,
        @RequestBody InstamartVariantRequest request
    ) {
        return ApiResponse.ok("Variant updated", service.updateVariant(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        service.deactivateVariant(id);
        return ApiResponse.ok("Variant deactivated", null);
    }
}
