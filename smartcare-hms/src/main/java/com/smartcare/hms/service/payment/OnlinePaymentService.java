package com.smartcare.hms.service.payment;

import org.springframework.stereotype.Service;

/** POLYMORPHISM: concrete override #3 of PaymentService. */
@Service("onlinePaymentService")
public class OnlinePaymentService implements PaymentService {

    @Override
    public String processPayment(Long billId, double amount) {
        // In a real system this would redirect to an online payment gateway (e.g. PayHere).
        return "Online payment of Rs. " + amount + " initiated for bill #" + billId + ". Awaiting gateway confirmation.";
    }

    @Override
    public String getPaymentMethodName() {
        return "ONLINE";
    }
}
