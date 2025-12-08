package com.nn.spring_simple_e_commerce_rest_api.payment.api.response;

public record PaymentResponse(
        String status,
        String message,
        String sessionId,
        String sessionUrl
) {
}
