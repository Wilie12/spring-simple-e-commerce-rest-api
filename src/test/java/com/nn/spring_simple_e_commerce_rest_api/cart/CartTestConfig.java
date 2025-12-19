package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartTestConfig {
    @Bean
    public CartService cartService(
            CartRepository cartRepository,
            CartMapper cartMapper,
            ProductService productService
    ) {
        return new CartService(cartRepository, cartMapper, productService);
    }
}
