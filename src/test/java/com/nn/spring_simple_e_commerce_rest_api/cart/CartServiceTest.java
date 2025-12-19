package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.config.TestSecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestSecurityConfig.class, CartTestConfig.class})
public class CartServiceTest {
    @MockitoBean
    private CartRepository cartRepository;
    @MockitoBean
    private CartMapper cartMapper;
    @MockitoBean
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void createCartShouldReturnCartForUser() {
        // given
        Cart mockCart = new Cart("alice");
        CartResponse mockResponse = new CartResponse("alice", Collections.emptyMap());
        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toCartResponse(any(Cart.class), any())).thenReturn(mockResponse);

        // when
        CartResponse actualResponse = cartService.createCart("alice");

        // then
        assertThat(actualResponse).isEqualTo(mockResponse);
    }

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    void createCartShouldNotWorkForUnauthorizedUser() {
        assertThrows(AuthorizationDeniedException.class, () -> cartService.createCart("alice"));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void getCartShouldReturnCartForUser() {
        // given
        Cart mockCart = new Cart("alice");
        CartResponse mockResponse = new CartResponse("alice", Collections.emptyMap());
        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toCartResponse(any(Cart.class), any())).thenReturn(mockResponse);

        // when
        CartResponse actualResponse = cartService.getCart("alice");

        // then
        assertThat(actualResponse).isEqualTo(mockResponse);
    }

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    void getCartShouldNotWorkForUnauthorizedUser() {
        assertThrows(AuthorizationDeniedException.class, () -> cartService.getCart("alice"));
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void addProductToCartShouldReturnUpdatedCart() {
        // given
        Cart mockCart = new Cart("alice");
        mockCart.addProduct(1L, 1);
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
        CartResponse mockCartResponse = new CartResponse("alice", Map.of(mockProductResponse, 1));

        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toCartResponse(any(Cart.class), any())).thenReturn(mockCartResponse);

        // when
        CartResponse actualResponse = cartService.addProductToCart("alice", 1L, 1);

        // then
        assertThat(actualResponse).isEqualTo(mockCartResponse);
    }

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    void addProductToCartShouldNotWorkForUnauthorizedUser() {
        assertThrows(
                AuthorizationDeniedException.class,
                () -> cartService.addProductToCart("alice", 1L, 1)
        );
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void clearCartShouldReturnEmptyCart() {
        // given
        Cart mockCart = new Cart("alice");
        mockCart.addProduct(1L, 1);
        CartResponse mockResponse = new CartResponse("alice", Collections.emptyMap());

        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toCartResponse(any(Cart.class), any())).thenReturn(mockResponse);

        // when
        CartResponse actualResponse = cartService.clearCart("alice");

        // then
        assertThat(actualResponse.products()).isEmpty();
    }

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    void clearCartShouldNotWorkForUnauthorizedUser() {
        assertThrows(AuthorizationDeniedException.class, () -> cartService.clearCart("alice"));
    }
}
