package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Admission;
import com.smartcare.hms.entity.Room;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.exception.RoomNotAvailableException;
import com.smartcare.hms.repository.AdmissionRepository;
import com.smartcare.hms.repository.RoomRepository;
import com.smartcare.hms.service.AdmissionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Business rule enforced here (Task 05 / Admission & Room Management):
 * "Rooms cannot be allocated if unavailable" + room availability tracking.
 */
@Service
public class AdmissionServiceImpl implements AdmissionService {

    private final AdmissionRepository admissionRepository;
    private final RoomRepository roomRepository;

    public AdmissionServiceImpl(AdmissionRepository admissionRepository, RoomRepository roomRepository) {
        this.admissionRepository = admissionRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Admission admitPatient(Admission admission) {
        if (admission.getPatient() == null || admission.getRoom() == null) {
            throw new InvalidInputException("Patient and room must be specified for an admission");
        }
        Room room = roomRepository.findById(admission.getRoom().getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is not available");
        }

        admission.setAdmissionDate(
                admission.getAdmissionDate() == null ? LocalDate.now() : admission.getAdmissionDate());
        admission.setAdmissionStatus(Admission.AdmissionStatus.ADMITTED);
        admission.setRoom(room);

        room.setAvailable(false); // Occupy the room
        roomRepository.save(room);

        return admissionRepository.save(admission);
    }

    @Override
    public Admission dischargePatient(Long admissionId) {
        Admission admission = getAdmissionById(admissionId);
        admission.setDischargeDate(LocalDate.now());
        admission.setAdmissionStatus(Admission.AdmissionStatus.DISCHARGED);

        Room room = admission.getRoom();
        room.setAvailable(true); // Free the room again
        roomRepository.save(room);

        return admissionRepository.save(admission);
    }

    @Override
    public Admission getAdmissionById(Long id) {
        return admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id: " + id));
    }

    @Override
    public List<Admission> getAllAdmissions() {
        return admissionRepository.findAll();
    }
}
