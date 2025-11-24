package com.nn.spring_simple_e_commerce_rest_api.product.support.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long productId) {
        super(String.format("Product with id %d not found", productId));
    }
}
