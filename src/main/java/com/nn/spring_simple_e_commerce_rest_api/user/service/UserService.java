package com.nn.spring_simple_e_commerce_rest_api.user.service;

import com.nn.spring_simple_e_commerce_rest_api.user.api.request.RegisterRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import com.nn.spring_simple_e_commerce_rest_api.user.repository.UserRepository;
import com.nn.spring_simple_e_commerce_rest_api.user.support.UserMapper;
import com.nn.spring_simple_e_commerce_rest_api.user.support.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User register(RegisterRequest registerRequest) {
        Optional<User> user = userRepository.findByUsername(registerRequest.username());

        if (user.isPresent()) {
            throw new UserAlreadyExistsException(registerRequest.username());
        }

        return userRepository.save(userMapper.toUser(registerRequest));
    }
}
