package com.nn.spring_simple_e_commerce_rest_api.user.support.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super(String.format("User with username %s already exists", username));
    }
}
