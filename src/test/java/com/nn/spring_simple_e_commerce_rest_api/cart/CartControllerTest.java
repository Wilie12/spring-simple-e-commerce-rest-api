package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.cart.controller.CartController;
import com.nn.spring_simple_e_commerce_rest_api.cart.service.CartService;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
public class CartControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    CartService cartService;
    @MockitoBean
    ProductService productService;
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

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void getCartShouldWork() throws Exception {
        mvc.perform(get("/api/v1/carts"))
                .andExpect(status().isOk());

        verify(cartService).getCart("alice");
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void addProductToCartShouldWork() throws Exception {
        // given
        Product product = new Product(
                "testName",
                "testDesc",
                "testFullDesc",
                2321,
                7,
                ProductCategory.OTHER,
                "testProducer"
        );
        ProductResponse mockResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getFullDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getProducer(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
        when(productService.getById(any(Long.class))).thenReturn(mockResponse);

        // when
        mvc.perform(post("/api/v1/carts/items/{productId}", product.getId())
                .queryParam("quantity", "7"))
                .andExpect(status().isOk());

        // then
        verify(productService).getById(product.getId());
        verify(cartService).addProductToCart("alice", product.getId(), 7);
    }

    @Test
    @WithMockUser(username = "alice", authorities = "USER")
    void clearCartShouldWork() throws Exception {
        mvc.perform(delete("/api/v1/carts"))
                .andExpect(status().isOk());

        verify(cartService).clearCart("alice");
    }
}
