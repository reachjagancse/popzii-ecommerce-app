package com.popzii.restaurant.service;

import com.popzii.restaurant.dto.*;
import java.util.List;

public interface RestaurantCatalogService {
    RestaurantResponse createRestaurant(RestaurantRequest request);
    List<RestaurantResponse> listRestaurants(String cuisine, java.math.BigDecimal minRating, int page, int size);
    RestaurantResponse updateRestaurant(Long id, RestaurantRequest request);
    void deactivateRestaurant(Long id);

    RestaurantMenuCategoryResponse createMenuCategory(RestaurantMenuCategoryRequest request);
    List<RestaurantMenuCategoryResponse> listMenuCategories(Long restaurantId, int page, int size);
    RestaurantMenuCategoryResponse updateMenuCategory(Long id, RestaurantMenuCategoryRequest request);
    void deactivateMenuCategory(Long id);

    RestaurantMenuItemResponse createMenuItem(RestaurantMenuItemRequest request);
    List<RestaurantMenuItemResponse> listMenuItems(Long categoryId, int page, int size);
    RestaurantMenuItemResponse updateMenuItem(Long id, RestaurantMenuItemRequest request);
    void deactivateMenuItem(Long id);
}
