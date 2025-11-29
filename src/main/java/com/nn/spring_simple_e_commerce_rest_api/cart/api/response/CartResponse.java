package com.nn.spring_simple_e_commerce_rest_api.cart.api.response;

import java.util.Map;

public record CartResponse(String username, Map<Long, Integer> products) {
}
