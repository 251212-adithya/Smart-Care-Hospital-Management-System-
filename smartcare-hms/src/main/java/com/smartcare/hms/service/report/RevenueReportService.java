package com.smartcare.hms.service.report;

import com.smartcare.hms.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RevenueReportService implements ReportService {

    private final BillRepository billRepository;

    public RevenueReportService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public Map<String, Object> generateReport() {
        double totalRevenue = billRepository.findAll().stream()
                .filter(b -> b.getPaymentStatus() == com.smartcare.hms.entity.Bill.PaymentStatus.PAID)
                .mapToDouble(com.smartcare.hms.entity.Bill::getTotalAmount)
                .sum();

        long unpaidCount = billRepository.findByPaymentStatus(
                com.smartcare.hms.entity.Bill.PaymentStatus.UNPAID).size();

        Map<String, Object> report = new HashMap<>();
        report.put("totalRevenueCollected", totalRevenue);
        report.put("unpaidBillsCount", unpaidCount);
        return report;
    }

    @Override
    public String getReportName() {
        return "Revenue Summary Report";
    }
}
