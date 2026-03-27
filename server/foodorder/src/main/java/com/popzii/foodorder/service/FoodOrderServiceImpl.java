package com.popzii.foodorder.service;

import com.popzii.foodorder.domain.FoodOrder;
import com.popzii.foodorder.domain.FoodOrderItem;
import com.popzii.foodorder.dto.*;
import com.popzii.foodorder.repository.FoodOrderItemRepository;
import com.popzii.foodorder.repository.FoodOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodOrderServiceImpl implements FoodOrderService {
    private final FoodOrderRepository orderRepo;
    private final FoodOrderItemRepository itemRepo;

    public FoodOrderServiceImpl(FoodOrderRepository orderRepo, FoodOrderItemRepository itemRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    public FoodOrderResponse createOrder(FoodOrderRequest request) {
        FoodOrder order = new FoodOrder();
        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());
        order.setStatus("PLACED");
        order.setPaymentStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        BigDecimal total = request.getItems().stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        FoodOrder saved = orderRepo.save(order);
        for (FoodOrderItemRequest itemRequest : request.getItems()) {
            FoodOrderItem item = new FoodOrderItem();
            item.setOrderId(saved.getId());
            item.setMenuItemId(itemRequest.getMenuItemId());
            item.setName(itemRequest.getName());
            item.setPrice(itemRequest.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            itemRepo.save(item);
        }
        return toResponse(saved);
    }

    @Override
    public FoodOrderResponse getOrder(Long id) {
        FoodOrder order = orderRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return toResponse(order);
    }

    @Override
    public List<FoodOrderResponse> listOrders() {
        return orderRepo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public FoodOrderResponse updateStatus(Long id, String status) {
        FoodOrder order = orderRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        return toResponse(orderRepo.save(order));
    }

    private FoodOrderResponse toResponse(FoodOrder order) {
        FoodOrderResponse response = new FoodOrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setRestaurantId(order.getRestaurantId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setCreatedAt(order.getCreatedAt());

        List<FoodOrderItemResponse> items = itemRepo.findByOrderId(order.getId()).stream()
            .map(item -> {
                FoodOrderItemResponse itemResponse = new FoodOrderItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setMenuItemId(item.getMenuItemId());
                itemResponse.setName(item.getName());
                itemResponse.setPrice(item.getPrice());
                itemResponse.setQuantity(item.getQuantity());
                return itemResponse;
            })
            .collect(Collectors.toList());
        response.setItems(items);
        return response;
    }
}
