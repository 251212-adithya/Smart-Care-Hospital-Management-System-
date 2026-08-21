package com.smartcare.hms.service.payment;

import com.smartcare.hms.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Simple factory that returns the correct PaymentService implementation
 * at runtime based on the requested payment method - this is where
 * POLYMORPHISM actually gets exercised (caller only knows the interface).
 */
@Component
public class PaymentServiceFactory {

    private final PaymentService cashPaymentService;
    private final PaymentService cardPaymentService;
    private final PaymentService onlinePaymentService;

    public PaymentServiceFactory(@Qualifier("cashPaymentService") PaymentService cashPaymentService,
                                  @Qualifier("cardPaymentService") PaymentService cardPaymentService,
                                  @Qualifier("onlinePaymentService") PaymentService onlinePaymentService) {
        this.cashPaymentService = cashPaymentService;
        this.cardPaymentService = cardPaymentService;
        this.onlinePaymentService = onlinePaymentService;
    }

    public PaymentService getPaymentService(String method) {
        if (method == null) throw new InvalidInputException("Payment method must be provided");
        return switch (method.toUpperCase()) {
            case "CASH" -> cashPaymentService;
            case "CARD" -> cardPaymentService;
            case "ONLINE" -> onlinePaymentService;
            default -> throw new InvalidInputException("Unsupported payment method: " + method);
        };
    }
}
