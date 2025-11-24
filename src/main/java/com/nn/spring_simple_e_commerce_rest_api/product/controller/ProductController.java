package com.nn.spring_simple_e_commerce_rest_api.product.controller;

import com.nn.spring_simple_e_commerce_rest_api.product.api.request.ProductRequest;
import com.nn.spring_simple_e_commerce_rest_api.product.api.response.ProductResponse;
import com.nn.spring_simple_e_commerce_rest_api.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.create(productRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<ProductResponse> products = productService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getById(@PathVariable long productId) {
        ProductResponse product = productService.getById(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(product);
    }
}
