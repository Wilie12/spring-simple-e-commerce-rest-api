package com.nn.spring_simple_e_commerce_rest_api.product;

import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                23.21,
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
                          "price": 23.21,
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
}
