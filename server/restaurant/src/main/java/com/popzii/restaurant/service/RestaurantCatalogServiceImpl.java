package com.popzii.restaurant.service;

import com.popzii.restaurant.domain.*;
import com.popzii.restaurant.dto.*;
import com.popzii.restaurant.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantCatalogServiceImpl implements RestaurantCatalogService {
    private final RestaurantRepository restaurantRepo;
    private final RestaurantMenuCategoryRepository categoryRepo;
    private final RestaurantMenuItemRepository itemRepo;

    public RestaurantCatalogServiceImpl(
        RestaurantRepository restaurantRepo,
        RestaurantMenuCategoryRepository categoryRepo,
        RestaurantMenuItemRepository itemRepo
    ) {
        this.restaurantRepo = restaurantRepo;
        this.categoryRepo = categoryRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest request) {
        validateName(request.getName(), "Restaurant name is required");
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(request.getOwnerId());
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setRating(request.getRating());
        if (request.getIsActive() != null) {
            restaurant.setIsActive(request.getIsActive());
        }
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(LocalDateTime.now());
        return toRestaurantResponse(restaurantRepo.save(restaurant));
    }

    @Override
    public List<RestaurantResponse> listRestaurants(String cuisine, java.math.BigDecimal minRating, int page, int size) {
        if (cuisine != null && minRating != null) {
            return restaurantRepo.findByCuisineIgnoreCaseAndRatingGreaterThanEqual(cuisine, minRating, PageRequest.of(page, size))
                .stream().map(this::toRestaurantResponse).collect(Collectors.toList());
        }
        if (cuisine != null) {
            return restaurantRepo.findByCuisineIgnoreCase(cuisine, PageRequest.of(page, size))
                .stream().map(this::toRestaurantResponse).collect(Collectors.toList());
        }
        if (minRating != null) {
            return restaurantRepo.findByRatingGreaterThanEqual(minRating, PageRequest.of(page, size))
                .stream().map(this::toRestaurantResponse).collect(Collectors.toList());
        }
        return restaurantRepo.findAll(PageRequest.of(page, size)).stream()
            .map(this::toRestaurantResponse).collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest request) {
        Restaurant restaurant = restaurantRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        if (request.getOwnerId() != null) {
            restaurant.setOwnerId(request.getOwnerId());
        }
        if (request.getName() != null) {
            restaurant.setName(request.getName());
        }
        if (request.getDescription() != null) {
            restaurant.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            restaurant.setAddress(request.getAddress());
        }
        if (request.getCuisine() != null) {
            restaurant.setCuisine(request.getCuisine());
        }
        if (request.getRating() != null) {
            restaurant.setRating(request.getRating());
        }
        if (request.getIsActive() != null) {
            restaurant.setIsActive(request.getIsActive());
        }
        restaurant.setUpdatedAt(LocalDateTime.now());
        return toRestaurantResponse(restaurantRepo.save(restaurant));
    }

    @Override
    public void deactivateRestaurant(Long id) {
        Restaurant restaurant = restaurantRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));
        restaurant.setIsActive(false);
        restaurant.setDeletedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(LocalDateTime.now());
        restaurantRepo.save(restaurant);
    }

    @Override
    public RestaurantMenuCategoryResponse createMenuCategory(RestaurantMenuCategoryRequest request) {
        validateName(request.getName(), "Menu category name is required");
        if (request.getRestaurantId() == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }
        RestaurantMenuCategory category = new RestaurantMenuCategory();
        category.setRestaurantId(request.getRestaurantId());
        category.setName(request.getName());
        category.setSortOrder(request.getSortOrder());
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return toMenuCategoryResponse(categoryRepo.save(category));
    }

    @Override
    public List<RestaurantMenuCategoryResponse> listMenuCategories(Long restaurantId, int page, int size) {
        if (restaurantId != null) {
            return categoryRepo.findByRestaurantId(restaurantId, PageRequest.of(page, size))
                .stream().map(this::toMenuCategoryResponse).collect(Collectors.toList());
        }
        return categoryRepo.findAll(PageRequest.of(page, size)).stream()
            .map(this::toMenuCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public RestaurantMenuCategoryResponse updateMenuCategory(Long id, RestaurantMenuCategoryRequest request) {
        RestaurantMenuCategory category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu category not found"));
        if (request.getRestaurantId() != null) {
            category.setRestaurantId(request.getRestaurantId());
        }
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        category.setUpdatedAt(LocalDateTime.now());
        return toMenuCategoryResponse(categoryRepo.save(category));
    }

    @Override
    public void deactivateMenuCategory(Long id) {
        RestaurantMenuCategory category = categoryRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu category not found"));
        category.setIsActive(false);
        category.setDeletedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepo.save(category);
    }

    @Override
    public RestaurantMenuItemResponse createMenuItem(RestaurantMenuItemRequest request) {
        validateName(request.getName(), "Menu item name is required");
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("Menu category is required");
        }
        RestaurantMenuItem item = new RestaurantMenuItem();
        item.setCategoryId(request.getCategoryId());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        if (request.getIsVeg() != null) {
            item.setIsVeg(request.getIsVeg());
        }
        if (request.getIsAvailable() != null) {
            item.setIsAvailable(request.getIsAvailable());
        }
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return toMenuItemResponse(itemRepo.save(item));
    }

    @Override
    public List<RestaurantMenuItemResponse> listMenuItems(Long categoryId, int page, int size) {
        if (categoryId != null) {
            return itemRepo.findByCategoryId(categoryId, PageRequest.of(page, size))
                .stream().map(this::toMenuItemResponse).collect(Collectors.toList());
        }
        return itemRepo.findAll(PageRequest.of(page, size)).stream()
            .map(this::toMenuItemResponse).collect(Collectors.toList());
    }

    @Override
    public RestaurantMenuItemResponse updateMenuItem(Long id, RestaurantMenuItemRequest request) {
        RestaurantMenuItem item = itemRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
        if (request.getCategoryId() != null) {
            item.setCategoryId(request.getCategoryId());
        }
        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getIsVeg() != null) {
            item.setIsVeg(request.getIsVeg());
        }
        if (request.getIsAvailable() != null) {
            item.setIsAvailable(request.getIsAvailable());
        }
        item.setUpdatedAt(LocalDateTime.now());
        return toMenuItemResponse(itemRepo.save(item));
    }

    @Override
    public void deactivateMenuItem(Long id) {
        RestaurantMenuItem item = itemRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Menu item not found"));
        item.setIsAvailable(false);
        item.setDeletedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        itemRepo.save(item);
    }

    private void validateName(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setOwnerId(restaurant.getOwnerId());
        response.setName(restaurant.getName());
        response.setDescription(restaurant.getDescription());
        response.setAddress(restaurant.getAddress());
        response.setCuisine(restaurant.getCuisine());
        response.setRating(restaurant.getRating());
        response.setIsActive(restaurant.getIsActive());
        response.setCreatedAt(restaurant.getCreatedAt());
        response.setUpdatedAt(restaurant.getUpdatedAt());
        return response;
    }

    private RestaurantMenuCategoryResponse toMenuCategoryResponse(RestaurantMenuCategory category) {
        RestaurantMenuCategoryResponse response = new RestaurantMenuCategoryResponse();
        response.setId(category.getId());
        response.setRestaurantId(category.getRestaurantId());
        response.setName(category.getName());
        response.setSortOrder(category.getSortOrder());
        response.setIsActive(category.getIsActive());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }

    private RestaurantMenuItemResponse toMenuItemResponse(RestaurantMenuItem item) {
        RestaurantMenuItemResponse response = new RestaurantMenuItemResponse();
        response.setId(item.getId());
        response.setCategoryId(item.getCategoryId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setIsVeg(item.getIsVeg());
        response.setIsAvailable(item.getIsAvailable());
        response.setCreatedAt(item.getCreatedAt());
        response.setUpdatedAt(item.getUpdatedAt());
        return response;
    }
}
