package com.smartcare.hms.repository;

import com.smartcare.hms.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    List<Appointment> findByPatient_PatientId(Long patientId);

    // Used to detect double-booking for the SAME doctor at the SAME date & time
    List<Appointment> findByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
            Long doctorId, LocalDate appointmentDate, LocalTime appointmentTime);
}
