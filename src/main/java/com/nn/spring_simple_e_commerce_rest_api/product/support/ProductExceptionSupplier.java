package com.nn.spring_simple_e_commerce_rest_api.product.support;

import com.nn.spring_simple_e_commerce_rest_api.product.support.exception.ProductNotFoundException;

import java.util.function.Supplier;

public class ProductExceptionSupplier {

    public static Supplier<ProductNotFoundException> productNotFound(long productId) {
        return () -> new ProductNotFoundException(productId);
    }
}
