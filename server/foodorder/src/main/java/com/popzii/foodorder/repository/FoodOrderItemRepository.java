package com.popzii.foodorder.repository;

import com.popzii.foodorder.domain.FoodOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodOrderItemRepository extends JpaRepository<FoodOrderItem, Long> {
    List<FoodOrderItem> findByOrderId(Long orderId);
}
