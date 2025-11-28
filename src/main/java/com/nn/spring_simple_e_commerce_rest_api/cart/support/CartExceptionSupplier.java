package com.nn.spring_simple_e_commerce_rest_api.cart.support;

import com.nn.spring_simple_e_commerce_rest_api.cart.support.exception.CartNotFoundException;

import java.util.function.Supplier;

public class CartExceptionSupplier {

    public static Supplier<CartNotFoundException> cartNotFound(String username) {
        return () -> new CartNotFoundException(username);
    }
}
