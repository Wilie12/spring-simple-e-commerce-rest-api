package com.nn.spring_simple_e_commerce_rest_api.user.api.response;

public record LoginResponse(String token, long expiresIn) {
}
