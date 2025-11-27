package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.config.TestSecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    @Autowired
    private CartService cartService;

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void createCartShouldReturnCartForUser() {
        // given
        Cart mockCart = new Cart("alice");
        CartResponse mockResponse = new CartResponse("alice", mockCart.getProducts());
        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toCartResponse(any(String.class), any(Cart.class))).thenReturn(mockResponse);

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
}
