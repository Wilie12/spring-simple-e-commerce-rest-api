package com.nn.spring_simple_e_commerce_rest_api.user.support;

import com.nn.spring_simple_e_commerce_rest_api.user.api.request.RegisterRequest;
import com.nn.spring_simple_e_commerce_rest_api.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toUser(RegisterRequest registerRequest) {
        return new User(
                registerRequest.username(),
                passwordEncoder.encode(registerRequest.password())
        );
    }
}
