package com.smartcare.hms.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
public class Bill {

    public enum PaymentStatus { PAID, UNPAID, PARTIALLY_PAID }
    public enum PaymentMethod { CASH, CARD, ONLINE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    // Defaulted to today by BillServiceImpl.generateBill when the client omits it,
    // so no @NotNull here - that would reject the request before the service runs.
    @Column(name = "bill_date", nullable = false)
    private LocalDate billDate;

    @PositiveOrZero(message = "Consultation charge cannot be negative")
    @Column(name = "consultation_charge")
    private Double consultationCharge = 0.0;

    @PositiveOrZero(message = "Room charge cannot be negative")
    @Column(name = "room_charge")
    private Double roomCharge = 0.0;

    @PositiveOrZero(message = "Laboratory charge cannot be negative")
    @Column(name = "lab_charge")
    private Double labCharge = 0.0;

    @PositiveOrZero(message = "Medicine charge cannot be negative")
    @Column(name = "medicine_charge")
    private Double medicineCharge = 0.0;

    @PositiveOrZero(message = "Total amount cannot be negative")
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount = 0.0;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;

    public Bill() {}

    /** Business method - recalculates total from the four charge components. */
    public void recalculateTotal() {
        double c = consultationCharge == null ? 0 : consultationCharge;
        double r = roomCharge == null ? 0 : roomCharge;
        double l = labCharge == null ? 0 : labCharge;
        double m = medicineCharge == null ? 0 : medicineCharge;
        this.totalAmount = c + r + l + m;
    }

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }
    public Double getConsultationCharge() { return consultationCharge; }
    public void setConsultationCharge(Double consultationCharge) { this.consultationCharge = consultationCharge; }
    public Double getRoomCharge() { return roomCharge; }
    public void setRoomCharge(Double roomCharge) { this.roomCharge = roomCharge; }
    public Double getLabCharge() { return labCharge; }
    public void setLabCharge(Double labCharge) { this.labCharge = labCharge; }
    public Double getMedicineCharge() { return medicineCharge; }
    public void setMedicineCharge(Double medicineCharge) { this.medicineCharge = medicineCharge; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}
