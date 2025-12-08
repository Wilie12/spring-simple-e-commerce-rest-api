package com.nn.spring_simple_e_commerce_rest_api.payment.support.exception;

public class PaymentSessionNotCreatedException extends RuntimeException {
    public PaymentSessionNotCreatedException() {
        super("Unable to create payment session");
    }
}
