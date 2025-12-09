package com.nn.spring_simple_e_commerce_rest_api.product.api.response;

import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;

import java.time.Instant;

public record ProductResponse(
        long id,
        String name,
        String shortDescription,
        String fullDescription,
        long price,
        int quantity,
        ProductCategory category,
        String producer,
        Instant createdAt,
        Instant updatedAt
) {
}
