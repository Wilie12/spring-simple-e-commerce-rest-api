package com.nn.spring_simple_e_commerce_rest_api.cart.support.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String username) {
        super(String.format("Cart for username %s not found", username));
    }
}
