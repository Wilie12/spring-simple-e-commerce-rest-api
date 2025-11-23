package com.nn.spring_simple_e_commerce_rest_api.product;

import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.ProductCategory;
import com.nn.spring_simple_e_commerce_rest_api.product.repository.ProductRepository;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import com.nn.spring_simple_e_commerce_rest_api.product.support.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestSecurityConfig.class})
public class ProductServiceTest {
    @MockitoBean
    private ProductRepository productRepository;
    @MockitoBean
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;

    @Test
    @WithMockUser(username = "alice", authorities = "CREATE_PRODUCTS")
    void createProductShouldReturnTheSameData() {
        // given
        Product product = new Product(
                "testName",
                "testDesc",
                "testFullDesc",
                23.21,
                7,
                ProductCategory.OTHER,
                "testProducer"
        );
        ProductRequest productRequest = new ProductRequest(
                product.getName(),
                product.getShortDescription(),
                product.getFullDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getCategory(),
                product.getProducer()
        );
        ProductResponse mockResponse = new ProductResponse(
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
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toProductResponse(any())).thenReturn(mockResponse);

        // when
        ProductResponse actualResponse = productService.create(productRequest);

        // then
        assertThat(actualResponse).isEqualTo(mockResponse);
    }

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    void createProductShouldNotWorkWithoutAuthorities() {
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
        // then
        assertThrows(AuthorizationDeniedException.class, () -> productService.create(productRequest));
    }
}
