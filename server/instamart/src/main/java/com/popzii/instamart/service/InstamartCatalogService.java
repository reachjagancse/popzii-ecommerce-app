package com.popzii.instamart.service;

import com.popzii.instamart.dto.*;
import java.util.List;

public interface InstamartCatalogService {
    InstamartCategoryResponse createCategory(InstamartCategoryRequest request);
    List<InstamartCategoryResponse> listCategories(int page, int size);
    InstamartCategoryResponse updateCategory(Long id, InstamartCategoryRequest request);
    void deactivateCategory(Long id);

    InstamartProductResponse createProduct(InstamartProductRequest request);
    List<InstamartProductResponse> listProducts(Long categoryId, String search, int page, int size);
    InstamartProductResponse updateProduct(Long id, InstamartProductRequest request);
    void deactivateProduct(Long id);

    InstamartVariantResponse createVariant(InstamartVariantRequest request);
    List<InstamartVariantResponse> listVariants(Long productId, int page, int size);
    InstamartVariantResponse updateVariant(Long id, InstamartVariantRequest request);
    void deactivateVariant(Long id);
}
