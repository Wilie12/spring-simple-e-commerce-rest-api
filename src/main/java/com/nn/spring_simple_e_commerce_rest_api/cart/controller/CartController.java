package com.nn.spring_simple_e_commerce_rest_api.cart.controller;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
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

    @PostMapping("/items/{productId}")
    public ResponseEntity<CartResponse> addProduct(
            Authentication authentication,
            @PathVariable long productId,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        ProductResponse product = productService.getById(productId);
        CartResponse cart = cartService.addProductToCart(authentication.getName(), product.id(), quantity);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cart);
    }
}
