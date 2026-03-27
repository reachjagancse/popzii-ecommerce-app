package com.popzii.instamart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InstamartVariantResponse {
    private Long id;
    private Long productId;
    private String name;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private Boolean isDefault;
    private Integer bulkMinQty;
    private BigDecimal bulkPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }

    public Integer getBulkMinQty() { return bulkMinQty; }
    public void setBulkMinQty(Integer bulkMinQty) { this.bulkMinQty = bulkMinQty; }

    public BigDecimal getBulkPrice() { return bulkPrice; }
    public void setBulkPrice(BigDecimal bulkPrice) { this.bulkPrice = bulkPrice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
