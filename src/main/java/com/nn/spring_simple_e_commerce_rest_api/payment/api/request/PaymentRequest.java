package com.nn.spring_simple_e_commerce_rest_api.payment.api.request;

public record PaymentRequest(
        long amount,
        long quantity,
        String name,
        String currency
) {
}
