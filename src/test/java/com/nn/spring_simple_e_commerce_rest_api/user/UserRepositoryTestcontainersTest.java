package com.nn.spring_simple_e_commerce_rest_api.user;

import com.nn.spring_simple_e_commerce_rest_api.config.TestcontainersConfiguration;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
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
public class UserRepositoryTestcontainersTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.saveAll(
                List.of(
                        new User("alice", "password"),
                        new User("bob", "password")
                )
        );
    }

    @Test
    public void findUserByUsernameShouldReturnCorrectUser() {
        Optional<User> user = userRepository.findByUsername("alice");
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo("alice");
    }
}
