package com.nn.spring_simple_e_commerce_rest_api.product.api.response;

import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;

import java.time.Instant;

public record ProductResponse(
        String name,
        String shortDescription,
        String fullDescription,
        double price,
        int quantity,
        ProductCategory category,
        String producer,
        Instant createdAt,
        Instant updatedAt
) {
}
