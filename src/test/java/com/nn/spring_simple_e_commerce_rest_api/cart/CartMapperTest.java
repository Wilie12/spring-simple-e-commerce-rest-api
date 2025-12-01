package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CartMapperTest {
    @MockitoBean
    ProductService productService;
    CartMapper cartMapper;

    @BeforeEach
    public void setUp() {
        cartMapper = new CartMapper(productService);
    }

    @Test
    public void mapCartToCartResponseShouldWork() {
        // given
        Product product = new Product(
                "testName",
                "testDesc",
                "testFullDesc",
                23.21,
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
        mockCart.addProduct(1L, 1);

        when(productService.getById(any(Long.class))).thenReturn(mockProductResponse);

        // when
        CartResponse actualResponse = cartMapper.toCartResponse(mockCart);

        // then
        assertThat(actualResponse.products().keySet()).contains(mockProductResponse);
        assertThat(actualResponse.username()).isEqualTo(mockCart.getUsername());
    }
}
