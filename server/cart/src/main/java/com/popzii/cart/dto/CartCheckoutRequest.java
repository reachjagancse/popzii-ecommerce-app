package com.popzii.cart.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CartCheckoutRequest {
    private Long userId;
    @Valid
    @Size(min = 1, message = "At least one item is required")
    private List<CartItemRequest> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<CartItemRequest> getItems() { return items; }
    public void setItems(List<CartItemRequest> items) { this.items = items; }
}
