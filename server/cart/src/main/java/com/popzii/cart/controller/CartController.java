package com.popzii.cart.controller;

import com.popzii.cart.dto.CartCheckoutRequest;
import com.popzii.cart.dto.CartResponse;
import com.popzii.cart.service.CartService;
import com.popzii.common.web.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService service;

    public CartController(CartService service) { this.service = service; }

    @PostMapping("/checkout")
    public ApiResponse<CartResponse> checkout(@Valid @RequestBody CartCheckoutRequest request) {
        return ApiResponse.ok("Checkout completed", service.checkout(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<CartResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("Cart fetched", service.getCart(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<CartResponse> updateStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ApiResponse.ok("Cart status updated", service.updateStatus(id, status));
    }
}
