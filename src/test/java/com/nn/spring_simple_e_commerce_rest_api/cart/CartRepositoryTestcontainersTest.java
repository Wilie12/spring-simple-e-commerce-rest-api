package com.nn.spring_simple_e_commerce_rest_api.cart;

import com.nn.spring_simple_e_commerce_rest_api.TestcontainersConfiguration;
import com.nn.spring_simple_e_commerce_rest_api.cart.domain.Cart;
import com.nn.spring_simple_e_commerce_rest_api.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
public class CartRepositoryTestcontainersTest {
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    public void setup() {
        cartRepository.saveAll(
                List.of(
                        new Cart("alice"),
                        new Cart("bob"),
                        new Cart("eva")
                )
        );
    }

    @Test
    public void findByUserShouldReturnCorrectUser() {
        Optional<Cart> cart = cartRepository.findByUsername("alice");

        assertThat(cart).isNotNull();
        assertThat(cart.get().getUsername()).isEqualTo("alice");
    }
}
