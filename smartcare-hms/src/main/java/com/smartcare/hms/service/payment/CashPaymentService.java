package com.smartcare.hms.service.payment;

import org.springframework.stereotype.Service;

/** POLYMORPHISM: concrete override #1 of PaymentService. */
@Service("cashPaymentService")
public class CashPaymentService implements PaymentService {

    @Override
    public String processPayment(Long billId, double amount) {
        return "Cash payment of Rs. " + amount + " received for bill #" + billId + ". Receipt issued at counter.";
    }

    @Override
    public String getPaymentMethodName() {
        return "CASH";
    }
}
