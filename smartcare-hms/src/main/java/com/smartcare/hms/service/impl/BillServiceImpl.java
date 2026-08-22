package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Bill;
import com.smartcare.hms.entity.Patient;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.BillRepository;
import com.smartcare.hms.repository.PatientRepository;
import com.smartcare.hms.service.BillService;
import com.smartcare.hms.service.payment.PaymentService;
import com.smartcare.hms.service.payment.PaymentServiceFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;
    private final PaymentServiceFactory paymentServiceFactory;

    public BillServiceImpl(BillRepository billRepository,
                           PatientRepository patientRepository,
                           PaymentServiceFactory paymentServiceFactory) {
        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
        this.paymentServiceFactory = paymentServiceFactory;
    }

    @Override
    @Transactional
    public Bill generateBill(Bill bill) {
        if (bill.getPatient() == null || bill.getPatient().getPatientId() == null) {
            throw new InvalidInputException("Bill must be linked to a patient");
        }
        if (anyNegative(bill)) {
            throw new InvalidInputException("Bill amounts cannot be negative");
        }

        // Resolve Patient from DB
        Long patientId = bill.getPatient().getPatientId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        bill.setPatient(patient);

        bill.setBillDate(bill.getBillDate() == null ? LocalDate.now() : bill.getBillDate());
        bill.recalculateTotal();
        bill.setPaymentStatus(Bill.PaymentStatus.UNPAID);
        return billRepository.save(bill);
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
    }

    @Override
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> getUnpaidBills() {
        return billRepository.findByPaymentStatus(Bill.PaymentStatus.UNPAID);
    }

    @Override
    @Transactional
    public String payBill(Long billId, String paymentMethod) {
        Bill bill = getBillById(billId);
        // POLYMORPHISM in action: we don't care which concrete class this is,
        // only that it fulfils the PaymentService contract.
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentMethod);
        String confirmation = paymentService.processPayment(billId, bill.getTotalAmount());

        bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        bill.setPaymentMethod(Bill.PaymentMethod.valueOf(paymentService.getPaymentMethodName()));
        billRepository.save(bill);

        return confirmation;
    }

    private boolean anyNegative(Bill bill) {
        return safe(bill.getConsultationCharge()) < 0 || safe(bill.getRoomCharge()) < 0
                || safe(bill.getLabCharge()) < 0 || safe(bill.getMedicineCharge()) < 0;
    }

    private double safe(Double value) {
        return value == null ? 0 : value;
    }
}
