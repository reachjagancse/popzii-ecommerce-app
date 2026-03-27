package com.popzii.product.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock = 0;

    @Column(name = "vendor_id")
    private Long vendorId;

    private String category;
    private String subcategory;

    public Product() {}

    public Product(String name, String description, BigDecimal price, Integer stock, Long vendorId, String category, String subcategory) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.vendorId = vendorId;
        this.category = category;
        this.subcategory = subcategory;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public Integer getStock() { return stock; }
    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }
    public void setStock(Integer stock) { this.stock = stock; }
}
