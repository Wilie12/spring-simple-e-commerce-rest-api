package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.config.TestSecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    void createCartShouldReturnCartForUser() {
        // given
        User mockUser = new User("alice", "password");
        Cart mockCart = new Cart("alice");
        CartResponse mockResponse = new CartResponse("alice", mockCart.getProducts());
        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toCartResponse(any(String.class), any(Cart.class))).thenReturn(mockResponse);

        // when
        CartResponse actualResponse = cartService.createCart(mockUser);

        // then
        assertThat(actualResponse).isEqualTo(mockResponse);
    }
}
