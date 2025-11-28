package com.nn.spring_simple_e_commerce_rest_api.cart.controller;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> create(Authentication authentication) {
        CartResponse cart = cartService.createCart(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cart);
    }

    @GetMapping
    public ResponseEntity<CartResponse> get(Authentication authentication) {
        CartResponse cart = cartService.getCart(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cart);
    }
}
