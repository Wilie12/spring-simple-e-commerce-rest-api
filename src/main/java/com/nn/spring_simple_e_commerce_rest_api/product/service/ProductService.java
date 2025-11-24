package com.nn.spring_simple_e_commerce_rest_api.product.service;

import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import com.nn.spring_simple_e_commerce_rest_api.product.repository.ProductRepository;
import com.nn.spring_simple_e_commerce_rest_api.product.support.ProductExceptionSupplier;
import com.nn.spring_simple_e_commerce_rest_api.product.support.ProductMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @PreAuthorize("hasAuthority('CREATE_PRODUCTS')")
    public ProductResponse create(ProductRequest productRequest) {
        Product product = productRepository.save(productMapper.toProduct(productRequest));
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getById(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductExceptionSupplier.productNotFound(productId));

        return productMapper.toProductResponse(product);
    }
}
