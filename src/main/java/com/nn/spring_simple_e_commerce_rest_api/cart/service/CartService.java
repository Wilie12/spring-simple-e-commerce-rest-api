package com.nn.spring_simple_e_commerce_rest_api.cart.service;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @PreAuthorize("#username == authentication.name")
    public CartResponse createCart(String username) {
        Optional<Cart> cart = cartRepository.findByUsername(username);

        if (cart.isPresent()) {
            return cartMapper.toCartResponse(username, cart.get());
        }

        Cart newCart = cartRepository.save(new Cart(username));
        return cartMapper.toCartResponse(username, newCart);
    }
}
