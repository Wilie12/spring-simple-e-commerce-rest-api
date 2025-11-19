package com.nn.spring_simple_e_commerce_rest_api.user.support;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.function.Supplier;

public class UserExceptionSupplier {

    public static Supplier<UsernameNotFoundException> usernameNotFound(String username) {
        return () -> new UsernameNotFoundException(
                String.format("User with username %s doesn't exist.", username)
        );
    }
}
