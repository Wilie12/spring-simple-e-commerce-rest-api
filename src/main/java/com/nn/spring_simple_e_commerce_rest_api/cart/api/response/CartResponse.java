package com.nn.spring_simple_e_commerce_rest_api.cart.api.response;

import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;

import java.util.Map;
import java.util.stream.LongStream;

public record CartResponse(String username, Map<ProductResponse, Integer> products) {

    public long getTotalPrice() {
        return products()
                .entrySet()
                .stream()
                .flatMapToLong(entry -> LongStream.of(entry.getKey().price() * entry.getValue()))
                .sum();
    }
}
