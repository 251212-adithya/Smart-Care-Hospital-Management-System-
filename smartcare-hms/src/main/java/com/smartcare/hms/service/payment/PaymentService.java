package com.smartcare.hms.service.payment;

/**
 * ABSTRACTION: defines a contract every payment method must fulfil,
 * without exposing HOW each one processes a payment.
 */
public interface PaymentService {
    /**
     * @param billId the bill being paid
     * @param amount the amount being paid
     * @return a confirmation / reference string
     */
    String processPayment(Long billId, double amount);

    String getPaymentMethodName();
}
