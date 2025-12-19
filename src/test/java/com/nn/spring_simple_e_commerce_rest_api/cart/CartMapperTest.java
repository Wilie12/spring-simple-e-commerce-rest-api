package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CartMapperTest {
    CartMapper cartMapper;

    @BeforeEach
    public void setup() {
        cartMapper = new CartMapper();
    }

    @Test
    public void mapCartToCartResponseShouldWork() {
        // given
        Product product = new Product(
                "testName",
                "testDesc",
                "testFullDesc",
                2321,
                7,
                ProductCategory.OTHER,
                "testProducer"
        );
        ProductResponse mockProductResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getFullDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getProducer(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
        Cart mockCart = new Cart("alice");
        mockCart.addProduct(product.getId(), 1);

        // when
        CartResponse actualResponse = cartMapper.toCartResponse(mockCart, List.of(mockProductResponse));

        // then
        assertThat(actualResponse.products().keySet()).contains(mockProductResponse);
        assertThat(actualResponse.username()).isEqualTo(mockCart.getUsername());
    }
}
