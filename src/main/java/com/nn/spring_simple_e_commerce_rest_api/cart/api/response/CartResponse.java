package com.nn.spring_simple_e_commerce_rest_api.cart.api.response;

import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;

import java.util.Map;

public record CartResponse(String username, Map<ProductResponse, Integer> products) {
}
