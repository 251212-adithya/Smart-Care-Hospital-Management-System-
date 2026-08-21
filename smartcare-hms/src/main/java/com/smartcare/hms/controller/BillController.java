package com.smartcare.hms.controller;

import com.smartcare.hms.entity.Bill;
import com.smartcare.hms.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<Bill> generate(@Valid @RequestBody Bill bill) {
        return new ResponseEntity<>(billService.generateBill(bill), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Bill getById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @GetMapping
    public List<Bill> getAll() {
        return billService.getAllBills();
    }

    @GetMapping("/unpaid")
    public List<Bill> unpaid() {
        return billService.getUnpaidBills();
    }

    /** Body example: { "paymentMethod": "CARD" } */
    @PutMapping("/{id}/pay")
    public String pay(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        return billService.payBill(id, payload.get("paymentMethod"));
    }
}
