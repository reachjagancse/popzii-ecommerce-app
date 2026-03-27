package com.popzii.cart.service;

import com.popzii.cart.domain.Cart;
import com.popzii.cart.domain.CartItem;
import com.popzii.cart.dto.*;
import com.popzii.cart.repository.CartItemRepository;
import com.popzii.cart.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    public CartServiceImpl(CartRepository cartRepo, CartItemRepository itemRepo) {
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    public CartResponse checkout(CartCheckoutRequest request) {
        Cart cart = new Cart();
        cart.setUserId(request.getUserId());
        cart.setStatus("CHECKED_OUT");
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        Cart saved = cartRepo.save(cart);

        for (CartItemRequest itemRequest : request.getItems()) {
            CartItem item = new CartItem();
            item.setCartId(saved.getId());
            item.setProductId(itemRequest.getProductId());
            item.setName(itemRequest.getName());
            item.setPrice(itemRequest.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            itemRepo.save(item);
        }
        return toResponse(saved);
    }

    @Override
    public CartResponse getCart(Long id) {
        Cart cart = cartRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        return toResponse(cart);
    }

    @Override
    public CartResponse updateStatus(Long id, String status) {
        Cart cart = cartRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        cart.setStatus(status);
        cart.setUpdatedAt(LocalDateTime.now());
        return toResponse(cartRepo.save(cart));
    }

    private CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setStatus(cart.getStatus());
        response.setCreatedAt(cart.getCreatedAt());

        List<CartItemResponse> items = itemRepo.findByCartId(cart.getId()).stream()
            .map(item -> {
                CartItemResponse itemResponse = new CartItemResponse();
                itemResponse.setId(item.getId());
                itemResponse.setProductId(item.getProductId());
                itemResponse.setName(item.getName());
                itemResponse.setPrice(item.getPrice());
                itemResponse.setQuantity(item.getQuantity());
                return itemResponse;
            })
            .collect(Collectors.toList());
        response.setItems(items);

        BigDecimal total = items.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalAmount(total);
        return response;
    }
}
