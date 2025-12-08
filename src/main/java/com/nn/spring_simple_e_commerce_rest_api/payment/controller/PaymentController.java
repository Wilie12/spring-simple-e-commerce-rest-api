package com.nn.spring_simple_e_commerce_rest_api.payment.controller;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.payment.api.request.PaymentRequest;
import com.nn.spring_simple_e_commerce_rest_api.payment.api.response.PaymentResponse;
import com.nn.spring_simple_e_commerce_rest_api.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final CartService cartService;

    public PaymentController(PaymentService paymentService, CartService cartService) {
        this.paymentService = paymentService;
        this.cartService = cartService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> checkout(Authentication authentication) {
        CartResponse cartResponse = cartService.getCart(authentication.getName());

        PaymentRequest paymentRequest = new PaymentRequest(
                cartResponse.getTotalPrice(),
                1L,
                "Checkout for " + authentication.getName(),
                "USD"
        );

        PaymentResponse paymentResponse = paymentService.checkout(paymentRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentResponse);
    }
}
