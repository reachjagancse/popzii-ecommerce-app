package com.popzii.restaurant.repository;

import com.popzii.restaurant.domain.RestaurantMenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem, Long> {
	Page<RestaurantMenuItem> findByCategoryId(Long categoryId, Pageable pageable);
}
