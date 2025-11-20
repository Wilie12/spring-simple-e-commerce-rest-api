package com.nn.spring_simple_e_commerce_rest_api.user.service;

import com.nn.spring_simple_e_commerce_rest_api.user.api.request.LoginRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.api.request.RegisterRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
import com.nn.spring_simple_e_commerce_rest_api.user.support.UserExceptionSupplier;
import com.nn.spring_simple_e_commerce_rest_api.user.support.UserMapper;
import com.nn.spring_simple_e_commerce_rest_api.user.support.exception.UserAlreadyExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    public User register(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByUsername(registerRequest.username());

        if (user.isPresent()) {
            throw new UserAlreadyExistsException(registerRequest.username());
        }

        return userRepository.save(userMapper.toUser(registerRequest));
    }

    public User login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        return userRepository.findByUsername(loginRequest.username())
                .orElseThrow(UserExceptionSupplier.usernameNotFound(loginRequest.username()));
    }
}
