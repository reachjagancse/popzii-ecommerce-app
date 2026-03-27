package com.popzii.restaurant.repository;

import com.popzii.restaurant.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Page<Restaurant> findByCuisineIgnoreCase(String cuisine, Pageable pageable);
	Page<Restaurant> findByRatingGreaterThanEqual(java.math.BigDecimal rating, Pageable pageable);
	Page<Restaurant> findByCuisineIgnoreCaseAndRatingGreaterThanEqual(String cuisine, java.math.BigDecimal rating, Pageable pageable);
}
