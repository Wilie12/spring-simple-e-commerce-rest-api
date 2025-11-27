package com.nn.spring_simple_e_commerce_rest_api.cart.support;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartResponse toCartResponse(String username, Cart cart) {
        return new CartResponse(
                username,
                cart.getProducts()
        );
    }
}
