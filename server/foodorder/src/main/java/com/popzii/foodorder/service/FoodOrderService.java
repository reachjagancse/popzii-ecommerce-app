package com.popzii.foodorder.service;

import com.popzii.foodorder.dto.FoodOrderRequest;
import com.popzii.foodorder.dto.FoodOrderResponse;

import java.util.List;

public interface FoodOrderService {
    FoodOrderResponse createOrder(FoodOrderRequest request);
    FoodOrderResponse getOrder(Long id);
    List<FoodOrderResponse> listOrders();
    FoodOrderResponse updateStatus(Long id, String status);
}
