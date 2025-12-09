package com.nn.spring_simple_e_commerce_rest_api.product;

import com.nn.spring_simple_e_commerce_rest_api.config.TestSecurityConfig;
import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductUpdateRequest;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestSecurityConfig.class, ProductTestConfig.class})
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
                2321,
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
                2321,
                7,
                ProductCategory.OTHER,
                "testProducer"
        );
        // then
        assertThrows(AuthorizationDeniedException.class, () -> productService.create(productRequest));
    }

    @Test
    void getAllShouldReturnAllProducts() {
        // given
        Product product1 = new Product(
                "testName",
                "testDesc",
                "testFullDesc",
                2321,
                7,
                ProductCategory.OTHER,
                "testProducer"
        );
        Product product2 = new Product(
                "testName2",
                "testDesc2",
                "testFullDesc2",
                7323,
                11,
                ProductCategory.HOME,
                "testProducer2"
        );
        ProductResponse mockResponse1 = new ProductResponse(
                product1.getId(),
                product1.getName(),
                product1.getShortDescription(),
                product1.getFullDescription(),
                product1.getPrice(),
                product1.getQuantity(),
                product1.getCategory(),
                product1.getProducer(),
                product1.getCreatedAt(),
                product1.getUpdatedAt()
        );
        ProductResponse mockResponse2 = new ProductResponse(
                product2.getId(),
                product2.getName(),
                product2.getShortDescription(),
                product2.getFullDescription(),
                product2.getPrice(),
                product2.getQuantity(),
                product2.getCategory(),
                product2.getProducer(),
                product2.getCreatedAt(),
                product2.getUpdatedAt()
        );
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productMapper.toProductResponse(any())).thenReturn(mockResponse1, mockResponse2);

        // when
        List<ProductResponse> actualResponse = productService.getAll();

        // then
        assertThat(actualResponse).hasSize(2);
        assertThat(actualResponse).isEqualTo(List.of(mockResponse1, mockResponse2));
    }

    @Test
    public void getByIdShouldReturnCorrectProduct() {
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
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(any())).thenReturn(mockResponse);

        // when
        ProductResponse actualResponse = productService.getById(1L);

        // then
        assertThat(actualResponse).isEqualTo(mockResponse);
    }

    @Test
    @WithMockUser(username = "alice", authorities = "UPDATE_PRODUCTS")
    public void updateShouldReturnUpdatedData() {
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
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(
                "updatedName",
                "updatedShortDesc",
                "updatedFullDesc",
                3042,
                10,
                ProductCategory.HOME,
                "updatedProducer"
        );
        ProductResponse mockResponse = new ProductResponse(
                product.getId(),
                productUpdateRequest.name(),
                productUpdateRequest.shortDescription(),
                productUpdateRequest.fullDescription(),
                productUpdateRequest.price(),
                productUpdateRequest.quantity(),
                productUpdateRequest.category(),
                productUpdateRequest.producer(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productMapper.toProductResponse(any())).thenReturn(mockResponse);

        // when
        ProductResponse actualResponse = productService.update(1L, productUpdateRequest);

        // then
        assertThat(actualResponse.name()).isEqualTo(productUpdateRequest.name());
        assertThat(actualResponse.shortDescription()).isEqualTo(productUpdateRequest.shortDescription());
        assertThat(actualResponse.fullDescription()).isEqualTo(productUpdateRequest.fullDescription());
        assertThat(actualResponse.price()).isEqualTo(productUpdateRequest.price());
        assertThat(actualResponse.quantity()).isEqualTo(productUpdateRequest.quantity());
        assertThat(actualResponse.category()).isEqualTo(productUpdateRequest.category());
        assertThat(actualResponse.producer()).isEqualTo(productUpdateRequest.producer());
        assertThat(actualResponse.createdAt()).isEqualTo(product.getCreatedAt());
        assertThat(actualResponse.updatedAt()).isEqualTo(product.getUpdatedAt());
    }

    @Test
    @WithMockUser(username = "bob", authorities = "USER")
    public void updateShouldNotWorkWithoutAuthorities() {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(
                "updatedName",
                "updatedShortDesc",
                "updatedFullDesc",
                3042,
                10,
                ProductCategory.HOME,
                "updatedProducer"
        );

        assertThrows(
                AuthorizationDeniedException.class,
                () -> productService.update(1L, productUpdateRequest)
        );
    }
}
