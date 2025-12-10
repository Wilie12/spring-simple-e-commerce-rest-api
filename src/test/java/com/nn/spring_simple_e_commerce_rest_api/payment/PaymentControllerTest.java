package com.nn.spring_simple_e_commerce_rest_api.payment;

import com.nn.spring_simple_e_commerce_rest_api.cart.api.response.CartResponse;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.payment.api.request.PaymentRequest;
import com.nn.spring_simple_e_commerce_rest_api.payment.controller.PaymentController;
import com.nn.spring_simple_e_commerce_rest_api.payment.service.PaymentService;
import com.nn.spring_simple_e_commerce_rest_api.shared.config.SecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.shared.service.JwtService;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@Import(SecurityConfig.class)
public class PaymentControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    CartService cartService;
    @MockitoBean
    PaymentService paymentService;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void checkoutPaymentShouldWork() throws Exception {
        // given
        CartResponse mockCartResponse = new CartResponse("alice", Collections.emptyMap());
        PaymentRequest mockPaymentRequest = new PaymentRequest(
                mockCartResponse.getTotalPrice(),
                1,
                "Checkout for " + mockCartResponse.username(),
                "USD"
        );

        when(cartService.getCart(any())).thenReturn(mockCartResponse);

        // when
        mvc.perform(post("/api/v1/payments/checkout"))
                .andExpect(status().isOk());

        verify(cartService).getCart(any());
        verify(paymentService).checkout(mockPaymentRequest);
        verify(cartService).clearCart(any());
    }

}
