package com.nn.spring_simple_e_commerce_rest_api.product.support;

import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest) {
        return new Product(
                productRequest.name(),
                productRequest.shortDescription(),
                productRequest.fullDescription(),
                productRequest.price(),
                productRequest.quantity(),
                productRequest.category(),
                productRequest.producer()
        );
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
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
    }
}
