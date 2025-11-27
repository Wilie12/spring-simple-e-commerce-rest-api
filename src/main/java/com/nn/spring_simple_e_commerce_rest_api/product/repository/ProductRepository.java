package com.nn.spring_simple_e_commerce_rest_api.product.repository;

import com.nn.spring_simple_e_commerce_rest_api.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
