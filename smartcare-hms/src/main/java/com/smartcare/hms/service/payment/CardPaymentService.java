package com.smartcare.hms.service.payment;

import org.springframework.stereotype.Service;

/** POLYMORPHISM: concrete override #2 of PaymentService. */
@Service("cardPaymentService")
public class CardPaymentService implements PaymentService {

    @Override
    public String processPayment(Long billId, double amount) {
        // In a real system this would call a card-processing gateway.
        return "Card payment of Rs. " + amount + " authorised for bill #" + billId + ".";
    }

    @Override
    public String getPaymentMethodName() {
        return "CARD";
    }
}
