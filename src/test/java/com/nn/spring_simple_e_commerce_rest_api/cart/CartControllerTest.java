package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.cart.controller.CartController;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
public class CartControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    CartService cartService;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void createCartShouldWork() throws Exception {
        mvc.perform(post("/api/v1/carts"))
                .andExpect(status().isCreated());

        verify(cartService).createCart("alice");
    }
}
