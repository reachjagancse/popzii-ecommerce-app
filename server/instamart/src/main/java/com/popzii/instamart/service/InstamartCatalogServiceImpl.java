package com.popzii.instamart.service;

import com.popzii.instamart.domain.*;
import com.popzii.instamart.dto.*;
import com.popzii.instamart.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstamartCatalogServiceImpl implements InstamartCatalogService {
    private final InstamartCategoryRepository categoryRepo;
    private final InstamartProductRepository productRepo;
    private final InstamartProductVariantRepository variantRepo;

    public InstamartCatalogServiceImpl(
        InstamartCategoryRepository categoryRepo,
        InstamartProductRepository productRepo,
        InstamartProductVariantRepository variantRepo
    ) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.variantRepo = variantRepo;
    }

    @Override
    public InstamartCategoryResponse createCategory(InstamartCategoryRequest request) {
        validateName(request.getName(), "Category name is required");
        InstamartCategory category = new InstamartCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return toCategoryResponse(categoryRepo.save(category));
    }

    @Override
    public List<InstamartCategoryResponse> listCategories(int page, int size) {
        return categoryRepo.findAll(PageRequest.of(page, size)).stream()
            .map(this::toCategoryResponse)
            .collect(Collectors.toList());
    }

    @Override
    public InstamartCategoryResponse updateCategory(Long id, InstamartCategoryRequest request) {
        InstamartCategory category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        category.setUpdatedAt(LocalDateTime.now());
        return toCategoryResponse(categoryRepo.save(category));
    }

    @Override
    public void deactivateCategory(Long id) {
        InstamartCategory category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        category.setIsActive(false);
        category.setDeletedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepo.save(category);
    }

    @Override
    public InstamartProductResponse createProduct(InstamartProductRequest request) {
        validateName(request.getName(), "Product name is required");
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        InstamartProduct product = new InstamartProduct();
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setUnit(request.getUnit());
        product.setBasePrice(request.getBasePrice());
        product.setDiscountPercent(request.getDiscountPercent());
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return toProductResponse(productRepo.save(product));
    }

    @Override
    public List<InstamartProductResponse> listProducts(Long categoryId, String search, int page, int size) {
        if (categoryId != null && search != null && !search.trim().isEmpty()) {
            return productRepo.findByCategoryIdAndNameContainingIgnoreCase(categoryId, search, PageRequest.of(page, size))
                .stream().map(this::toProductResponse).collect(Collectors.toList());
        }
        if (categoryId != null) {
            return productRepo.findByCategoryId(categoryId, PageRequest.of(page, size))
                .stream().map(this::toProductResponse).collect(Collectors.toList());
        }
        if (search != null && !search.trim().isEmpty()) {
            return productRepo.findByNameContainingIgnoreCase(search, PageRequest.of(page, size))
                .stream().map(this::toProductResponse).collect(Collectors.toList());
        }
        return productRepo.findAll(PageRequest.of(page, size)).stream()
            .map(this::toProductResponse).collect(Collectors.toList());
    }

    @Override
    public InstamartProductResponse updateProduct(Long id, InstamartProductRequest request) {
        InstamartProduct product = productRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getUnit() != null) {
            product.setUnit(request.getUnit());
        }
        if (request.getBasePrice() != null) {
            product.setBasePrice(request.getBasePrice());
        }
        if (request.getDiscountPercent() != null) {
            product.setDiscountPercent(request.getDiscountPercent());
        }
        if (request.getIsActive() != null) {
            product.setIsActive(request.getIsActive());
        }
        product.setUpdatedAt(LocalDateTime.now());
        return toProductResponse(productRepo.save(product));
    }

    @Override
    public void deactivateProduct(Long id) {
        InstamartProduct product = productRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setIsActive(false);
        product.setDeletedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
    }

    @Override
    public InstamartVariantResponse createVariant(InstamartVariantRequest request) {
        validateName(request.getName(), "Variant name is required");
        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Product is required");
        }
        InstamartProductVariant variant = new InstamartProductVariant();
        variant.setProductId(request.getProductId());
        variant.setName(request.getName());
        variant.setSku(request.getSku());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        if (request.getIsDefault() != null) {
            variant.setIsDefault(request.getIsDefault());
        }
        variant.setBulkMinQty(request.getBulkMinQty());
        variant.setBulkPrice(request.getBulkPrice());
        variant.setCreatedAt(LocalDateTime.now());
        variant.setUpdatedAt(LocalDateTime.now());
        return toVariantResponse(variantRepo.save(variant));
    }

    @Override
    public List<InstamartVariantResponse> listVariants(Long productId, int page, int size) {
        if (productId != null) {
            return variantRepo.findByProductId(productId, PageRequest.of(page, size))
                .stream().map(this::toVariantResponse).collect(Collectors.toList());
        }
        return variantRepo.findAll(PageRequest.of(page, size)).stream()
            .map(this::toVariantResponse).collect(Collectors.toList());
    }

    @Override
    public InstamartVariantResponse updateVariant(Long id, InstamartVariantRequest request) {
        InstamartProductVariant variant = variantRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Variant not found"));
        if (request.getProductId() != null) {
            variant.setProductId(request.getProductId());
        }
        if (request.getName() != null) {
            variant.setName(request.getName());
        }
        if (request.getSku() != null) {
            variant.setSku(request.getSku());
        }
        if (request.getPrice() != null) {
            variant.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            variant.setStock(request.getStock());
        }
        if (request.getIsDefault() != null) {
            variant.setIsDefault(request.getIsDefault());
        }
        if (request.getBulkMinQty() != null) {
            variant.setBulkMinQty(request.getBulkMinQty());
        }
        if (request.getBulkPrice() != null) {
            variant.setBulkPrice(request.getBulkPrice());
        }
        variant.setUpdatedAt(LocalDateTime.now());
        return toVariantResponse(variantRepo.save(variant));
    }

    @Override
    public void deactivateVariant(Long id) {
        InstamartProductVariant variant = variantRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Variant not found"));
        variant.setDeletedAt(LocalDateTime.now());
        variant.setUpdatedAt(LocalDateTime.now());
        variantRepo.save(variant);
    }

    private void validateName(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private InstamartCategoryResponse toCategoryResponse(InstamartCategory category) {
        InstamartCategoryResponse response = new InstamartCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setIsActive(category.getIsActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }

    private InstamartProductResponse toProductResponse(InstamartProduct product) {
        InstamartProductResponse response = new InstamartProductResponse();
        response.setId(product.getId());
        response.setCategoryId(product.getCategoryId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setBrand(product.getBrand());
        response.setUnit(product.getUnit());
        response.setBasePrice(product.getBasePrice());
        response.setDiscountPercent(product.getDiscountPercent());
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }

    private InstamartVariantResponse toVariantResponse(InstamartProductVariant variant) {
        InstamartVariantResponse response = new InstamartVariantResponse();
        response.setId(variant.getId());
        response.setProductId(variant.getProductId());
        response.setName(variant.getName());
        response.setSku(variant.getSku());
        response.setPrice(variant.getPrice());
        response.setStock(variant.getStock());
        response.setIsDefault(variant.getIsDefault());
        response.setBulkMinQty(variant.getBulkMinQty());
        response.setBulkPrice(variant.getBulkPrice());
        response.setCreatedAt(variant.getCreatedAt());
        response.setUpdatedAt(variant.getUpdatedAt());
        return response;
    }
}
