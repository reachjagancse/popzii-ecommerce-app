package com.popzii.restaurant.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.restaurant.dto.RestaurantMenuCategoryRequest;
import com.popzii.restaurant.dto.RestaurantMenuCategoryResponse;
import com.popzii.restaurant.service.RestaurantCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/menu-categories")
public class RestaurantMenuCategoryController {
    private final RestaurantCatalogService service;

    public RestaurantMenuCategoryController(RestaurantCatalogService service) { this.service = service; }

    @PostMapping
    public ApiResponse<RestaurantMenuCategoryResponse> create(@Valid @RequestBody RestaurantMenuCategoryRequest request) {
        return ApiResponse.ok("Menu category created", service.createMenuCategory(request));
    }

    @GetMapping
    public ApiResponse<List<RestaurantMenuCategoryResponse>> list(
        @RequestParam(required = false) Long restaurantId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok("Menu categories fetched", service.listMenuCategories(restaurantId, page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<RestaurantMenuCategoryResponse> update(
        @PathVariable Long id,
        @RequestBody RestaurantMenuCategoryRequest request
    ) {
        return ApiResponse.ok("Menu category updated", service.updateMenuCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        service.deactivateMenuCategory(id);
        return ApiResponse.ok("Menu category deactivated", null);
    }
}
