package com.popzii.restaurant.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.restaurant.dto.RestaurantMenuItemRequest;
import com.popzii.restaurant.dto.RestaurantMenuItemResponse;
import com.popzii.restaurant.service.RestaurantCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/menu-items")
public class RestaurantMenuItemController {
    private final RestaurantCatalogService service;

    public RestaurantMenuItemController(RestaurantCatalogService service) { this.service = service; }

    @PostMapping
    public ApiResponse<RestaurantMenuItemResponse> create(@Valid @RequestBody RestaurantMenuItemRequest request) {
        return ApiResponse.ok("Menu item created", service.createMenuItem(request));
    }

    @GetMapping
    public ApiResponse<List<RestaurantMenuItemResponse>> list(
        @RequestParam(required = false) Long categoryId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok("Menu items fetched", service.listMenuItems(categoryId, page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<RestaurantMenuItemResponse> update(
        @PathVariable Long id,
        @RequestBody RestaurantMenuItemRequest request
    ) {
        return ApiResponse.ok("Menu item updated", service.updateMenuItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        service.deactivateMenuItem(id);
        return ApiResponse.ok("Menu item deactivated", null);
    }
}
