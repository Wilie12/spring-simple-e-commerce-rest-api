package com.nn.spring_simple_e_commerce_rest_api.product.api.request;

import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;

public record ProductRequest(
        String name,
        String shortDescription,
        String fullDescription,
        long price,
        int quantity,
        ProductCategory category,
        String producer
) {
}
