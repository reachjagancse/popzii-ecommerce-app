package com.popzii.instamart.dto;

import jakarta.validation.constraints.NotBlank;

public class InstamartCategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
    private String description;
    private Boolean isActive;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
