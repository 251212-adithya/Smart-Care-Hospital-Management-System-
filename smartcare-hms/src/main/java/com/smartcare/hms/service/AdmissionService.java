package com.smartcare.hms.service;

import com.smartcare.hms.entity.Admission;
import java.util.List;

public interface AdmissionService {
    Admission admitPatient(Admission admission);
    Admission dischargePatient(Long admissionId);
    Admission getAdmissionById(Long id);
    List<Admission> getAllAdmissions();
}
