package com.nn.spring_simple_e_commerce_rest_api.payment.service;

import com.nn.spring_simple_e_commerce_rest_api.payment.api.request.PaymentRequest;
import com.nn.spring_simple_e_commerce_rest_api.payment.api.response.PaymentResponse;
import com.nn.spring_simple_e_commerce_rest_api.payment.support.exception.PaymentSessionNotCreatedException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Value("${stripe.secret-key}")
    private String secretKey;

    public PaymentResponse checkout(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(paymentRequest.name())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequest.currency())
                        .setUnitAmount(paymentRequest.amount())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(paymentRequest.quantity())
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("success")
                        .setCancelUrl("cancel")
                        .addLineItem(lineItem)
                        .build();

        Session session;

        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new PaymentSessionNotCreatedException();
        }
        
        return new PaymentResponse(
                "SUCCESS",
                "Payment session created",
                session.getId(),
                session.getUrl()
        );
    }
}
