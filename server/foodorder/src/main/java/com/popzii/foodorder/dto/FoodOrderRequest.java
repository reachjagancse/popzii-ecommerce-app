package com.popzii.foodorder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class FoodOrderRequest {
    private Long userId;
    @NotNull(message = "Restaurant is required")
    private Long restaurantId;
    @Valid
    @Size(min = 1, message = "At least one item is required")
    private List<FoodOrderItemRequest> items;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public List<FoodOrderItemRequest> getItems() { return items; }
    public void setItems(List<FoodOrderItemRequest> items) { this.items = items; }
}
