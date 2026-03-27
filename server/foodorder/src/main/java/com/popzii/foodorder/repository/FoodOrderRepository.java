package com.popzii.foodorder.repository;

import com.popzii.foodorder.domain.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
}
