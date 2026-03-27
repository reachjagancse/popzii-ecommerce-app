package com.popzii.restaurant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class RestaurantMenuCategoryRequest {
    @NotNull(message = "Restaurant is required")
    private Long restaurantId;
    @NotBlank(message = "Menu category name is required")
    private String name;
    @PositiveOrZero(message = "Sort order must be positive")
    private Integer sortOrder;
    private Boolean isActive;

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
