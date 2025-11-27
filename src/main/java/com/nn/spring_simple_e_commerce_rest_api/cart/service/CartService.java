package com.nn.spring_simple_e_commerce_rest_api.cart.service;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
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

    public CartResponse createCart(User user) {
        Optional<Cart> cart = cartRepository.findByUsername(user.getUsername());

        if (cart.isPresent()) {
            return cartMapper.toCartResponse(user.getUsername(), cart.get());
        }

        Cart newCart = cartRepository.save(new Cart(user.getUsername()));
        return cartMapper.toCartResponse(user.getUsername(), newCart);
    }
}
