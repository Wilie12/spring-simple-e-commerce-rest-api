package com.nn.spring_simple_e_commerce_rest_api.cart.api.response;

import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;

import java.util.Map;

public record CartResponse(String username, Map<Product, Integer> products) {
}
