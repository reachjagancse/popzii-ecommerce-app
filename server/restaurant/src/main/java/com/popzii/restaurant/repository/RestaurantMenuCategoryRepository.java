package com.popzii.restaurant.repository;

import com.popzii.restaurant.domain.RestaurantMenuCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantMenuCategoryRepository extends JpaRepository<RestaurantMenuCategory, Long> {
	Page<RestaurantMenuCategory> findByRestaurantId(Long restaurantId, Pageable pageable);
}
