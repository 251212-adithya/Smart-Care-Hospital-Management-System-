package com.smartcare.hms.service;

import com.smartcare.hms.entity.Bill;
import java.util.List;

public interface BillService {
    Bill generateBill(Bill bill);
    Bill getBillById(Long id);
    List<Bill> getAllBills();
    List<Bill> getUnpaidBills();
    String payBill(Long billId, String paymentMethod);
}
