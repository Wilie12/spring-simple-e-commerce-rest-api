package com.nn.spring_simple_e_commerce_rest_api.cart.service;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartExceptionSupplier;
import com.nn.spring_simple_e_commerce_rest_api.cart.support.CartMapper;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, CartMapper cartMapper, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.productService = productService;
    }

    @PreAuthorize("#username == authentication.name")
    public CartResponse createCart(String username) {
        Optional<Cart> cart = cartRepository.findByUsername(username);

        if (cart.isPresent()) {
            List<ProductResponse> products = productService.getByIds(cart.get().getProducts().keySet());
            return cartMapper.toCartResponse(cart.get(), products);
        }

        Cart newCart = cartRepository.save(new Cart(username));
        return cartMapper.toCartResponse(newCart, Collections.emptyList());
    }

    @PreAuthorize("#username == authentication.name")
    public CartResponse getCart(String username) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(CartExceptionSupplier.cartNotFound(username));

        List<ProductResponse> products = productService.getByIds(cart.getProducts().keySet());

        return cartMapper.toCartResponse(cart, products);
    }

    @PreAuthorize("#username == authentication.name")
    public CartResponse addProductToCart(String username, long productId, int quantity) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(CartExceptionSupplier.cartNotFound(username));

        cart.addProduct(productId, quantity);
        List<ProductResponse> products = productService.getByIds(cart.getProducts().keySet());

        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart, products);
    }

    @PreAuthorize("#username == authentication.name")
    public CartResponse clearCart(String username) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(CartExceptionSupplier.cartNotFound(username));

        cart.clearProducts();
        cartRepository.save(cart);
        return cartMapper.toCartResponse(cart, Collections.emptyList());
    }
}
