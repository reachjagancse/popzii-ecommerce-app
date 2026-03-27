package com.popzii.foodorder.controller;

import com.popzii.common.web.ApiResponse;
import com.popzii.foodorder.dto.FoodOrderRequest;
import com.popzii.foodorder.dto.FoodOrderResponse;
import com.popzii.foodorder.service.FoodOrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foodorders")
public class FoodOrderController {
    private final FoodOrderService service;

    public FoodOrderController(FoodOrderService service) { this.service = service; }

    @PostMapping
    public ApiResponse<FoodOrderResponse> create(@Valid @RequestBody FoodOrderRequest request) {
        return ApiResponse.ok("Order created", service.createOrder(request));
    }

    @GetMapping
    public ApiResponse<List<FoodOrderResponse>> list() {
        return ApiResponse.ok("Orders fetched", service.listOrders());
    }

    @GetMapping("/{id}")
    public ApiResponse<FoodOrderResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("Order fetched", service.getOrder(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<FoodOrderResponse> updateStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ApiResponse.ok("Order status updated", service.updateStatus(id, status));
    }
}
