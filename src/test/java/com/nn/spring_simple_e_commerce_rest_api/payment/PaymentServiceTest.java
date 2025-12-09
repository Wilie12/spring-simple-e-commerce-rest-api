package com.nn.spring_simple_e_commerce_rest_api.payment;

import com.nn.spring_simple_e_commerce_rest_api.payment.api.request.PaymentRequest;
import com.nn.spring_simple_e_commerce_rest_api.payment.api.response.PaymentResponse;
import com.nn.spring_simple_e_commerce_rest_api.payment.service.PaymentService;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource({"stripe.secret-key=secret_key"})
public class PaymentServiceTest {
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        paymentService = new PaymentService();
    }

    @Test
    public void checkoutPaymentShouldReturnSuccessResponse() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest(
                12345,
                1,
                "checkout for user",
                "USD"
        );

        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn("sessionId");
        when(mockSession.getUrl()).thenReturn("successUrl");

        // when
        try (MockedStatic<Session> mockedStaticSession = Mockito.mockStatic(Session.class)) {
            mockedStaticSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);

            PaymentResponse actualResponse = paymentService.checkout(paymentRequest);

            // then
            assertThat(actualResponse.status()).isEqualTo("SUCCESS");
            assertThat(actualResponse.message()).isEqualTo("Payment session created");
            assertThat(actualResponse.sessionId()).isEqualTo("sessionId");
            assertThat(actualResponse.sessionUrl()).isEqualTo("successUrl");
        }
    }
}