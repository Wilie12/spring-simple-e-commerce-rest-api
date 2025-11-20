package com.nn.spring_simple_e_commerce_rest_api.user;

import com.nn.spring_simple_e_commerce_rest_api.user.api.request.LoginRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.api.request.RegisterRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
import com.nn.spring_simple_e_commerce_rest_api.user.service.UserService;
import com.nn.spring_simple_e_commerce_rest_api.user.support.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, userMapper, authenticationManager);
    }

    @Test
    public void createUserShouldReturnUserWithHashedPassword() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("alice", "password");
        when(userMapper.toUser(any())).thenReturn(new User("alice", "hashed_password"));
        when(userRepository.save(any())).thenReturn(new User("alice", "hashed_password"));

        // when
        User user = userService.register(registerRequest);

        // then
        assertThat(user.getUsername()).isEqualTo(registerRequest.username());
        assertThat(user.getPassword()).isNotEqualTo(registerRequest.password());
    }

    @Test
    public void loginUserShouldReturnCorrectUser() {
        // given
        LoginRequest loginRequest = new LoginRequest("alice", "password");
        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(new User("alice", "hashed_password")));

        // when
        User user = userService.login(loginRequest);

        // then
        assertThat(user.getUsername()).isEqualTo(loginRequest.username());
        assertThat(user.getPassword()).isNotEqualTo(loginRequest.password());
    }
}
