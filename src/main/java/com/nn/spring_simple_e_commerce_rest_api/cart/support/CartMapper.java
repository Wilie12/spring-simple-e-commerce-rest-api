package com.nn.spring_simple_e_commerce_rest_api.cart.support;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        return new CartResponse(
                cart.getUsername(),
                cart.getProducts()
        );
    }
}
