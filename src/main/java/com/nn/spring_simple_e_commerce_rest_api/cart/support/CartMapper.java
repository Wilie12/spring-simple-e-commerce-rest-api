package com.nn.spring_simple_e_commerce_rest_api.cart.support;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart, List<ProductResponse> products) {
        Map<ProductResponse, Integer> productsWithQuantities = products
                .stream()
                .collect(Collectors.toMap(
                        productResponse -> productResponse,
                        productResponse -> cart.getProducts().get(productResponse.id())
                ));

        return new CartResponse(cart.getUsername(), productsWithQuantities);
    }
}
