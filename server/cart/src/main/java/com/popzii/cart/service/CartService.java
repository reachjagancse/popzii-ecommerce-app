package com.popzii.cart.service;

import com.popzii.cart.dto.CartCheckoutRequest;
import com.popzii.cart.dto.CartResponse;

public interface CartService {
    CartResponse checkout(CartCheckoutRequest request);
    CartResponse getCart(Long id);
    CartResponse updateStatus(Long id, String status);
}
