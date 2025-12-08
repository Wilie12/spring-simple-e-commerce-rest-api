package com.nn.spring_simple_e_commerce_rest_api.product;

import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductUpdateRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.controller.ProductController;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import com.nn.spring_simple_e_commerce_rest_api.shared.config.SecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.shared.service.JwtService;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mvc;
    @MockitoBean
    ProductService productService;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "alice", authorities = "CREATE_PRODUCTS")
    void createProductShouldWork() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest(
                "testName",
                "testDesc",
                "testFullDesc",
                2321,
                7,
                ProductCategory.OTHER,
                "testProducer"
        );

        // when
        mvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "testName",
                          "shortDescription": "testDesc",
                          "fullDescription": "testFullDesc",
                          "price": 2321,
                          "quantity": 7,
                          "category": "OTHER",
                          "producer": "testProducer"
                        }
                        """))
                .andExpect(status().isCreated());

        // then
        verify(productService).create(productRequest);
    }

    @Test
    void getAllProductsShouldWork() throws Exception {
        mvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());

        verify(productService).getAll();
    }

    @Test
    void getProductByIdShouldWork() throws Exception {
        mvc.perform(get("/api/v1/products/{productId}", 1L))
                .andExpect(status().isOk());

        verify(productService).getById(1L);
    }

    @Test
    @WithMockUser(username = "alice", authorities = "UPDATE_PRODUCTS")
    void updateProductShouldWork() throws Exception {
        // given
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(
                "updatedName",
                "updatedShortDesc",
                "updatedFullDesc",
                3042,
                10,
                ProductCategory.HOME,
                "updatedProducer"
        );

        // when
        mvc.perform(put("/api/v1/products/{productId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "updatedName",
                          "shortDescription": "updatedShortDesc",
                          "fullDescription": "updatedFullDesc",
                          "price": 3042,
                          "quantity": 10,
                          "category": "HOME",
                          "producer": "updatedProducer"
                        }
                        """))
                .andExpect(status().isOk());

        // then
        verify(productService).update(1L, productUpdateRequest);
    }
}
