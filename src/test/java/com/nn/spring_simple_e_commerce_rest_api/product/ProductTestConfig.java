package com.nn.spring_simple_e_commerce_rest_api.product;

import com.nn.spring_simple_e_commerce_rest_api.product.repository.ProductRepository;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import com.nn.spring_simple_e_commerce_rest_api.product.support.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductTestConfig {
    @Bean
    public ProductService productService(ProductRepository productRepository, ProductMapper productMapper) {
        return new ProductService(productRepository, productMapper);
    }
}
