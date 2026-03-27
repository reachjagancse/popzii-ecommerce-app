package com.popzii.restaurant.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.restaurant.dto.RestaurantRequest;
import com.popzii.restaurant.dto.RestaurantResponse;
import com.popzii.restaurant.service.RestaurantCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantCatalogService service;

    public RestaurantController(RestaurantCatalogService service) { this.service = service; }

    @PostMapping
    public ApiResponse<RestaurantResponse> create(@Valid @RequestBody RestaurantRequest request) {
        return ApiResponse.ok("Restaurant created", service.createRestaurant(request));
    }

    @GetMapping
    public ApiResponse<List<RestaurantResponse>> list(
        @RequestParam(required = false) String cuisine,
        @RequestParam(required = false) BigDecimal minRating,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return ApiResponse.ok("Restaurants fetched", service.listRestaurants(cuisine, minRating, page, size));
    }

    @PutMapping("/{id}")
    public ApiResponse<RestaurantResponse> update(
        @PathVariable Long id,
        @RequestBody RestaurantRequest request
    ) {
        return ApiResponse.ok("Restaurant updated", service.updateRestaurant(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deactivate(@PathVariable Long id) {
        service.deactivateRestaurant(id);
        return ApiResponse.ok("Restaurant deactivated", null);
    }
}
