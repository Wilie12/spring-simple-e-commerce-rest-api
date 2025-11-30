package com.nn.spring_simple_e_commerce_rest_api.cart.support;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    ProductService productService;

    public CartMapper(ProductService productService) {
        this.productService = productService;
    }

    public CartResponse toCartResponse(Cart cart) {
        return new CartResponse(
                cart.getUsername(),
                cart.getProducts()
                        .keySet()
                        .stream()
                        .collect(Collectors.toMap(productService::getById, Long::intValue))
        );
    }
}
