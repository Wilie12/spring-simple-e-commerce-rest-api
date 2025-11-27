package com.nn.spring_simple_e_commerce_rest_api.cart.repository;

import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    Optional<Cart> findByUsername(String username);
}
