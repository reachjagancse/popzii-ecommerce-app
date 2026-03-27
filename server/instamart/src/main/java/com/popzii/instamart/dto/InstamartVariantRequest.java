package com.popzii.instamart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class InstamartVariantRequest {
    @NotNull(message = "Product is required")
    private Long productId;
    @NotBlank(message = "Variant name is required")
    private String name;
    private String sku;
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive")
    private BigDecimal price;
    @PositiveOrZero(message = "Stock must be positive")
    private Integer stock;
    private Boolean isDefault;
    @PositiveOrZero(message = "Bulk min qty must be positive")
    private Integer bulkMinQty;
    @PositiveOrZero(message = "Bulk price must be positive")
    private BigDecimal bulkPrice;

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
}
