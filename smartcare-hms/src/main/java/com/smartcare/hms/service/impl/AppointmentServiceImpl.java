package com.smartcare.hms.service.impl;

import com.smartcare.hms.entity.Appointment;
import com.smartcare.hms.entity.Doctor;
import com.smartcare.hms.entity.Patient;
import com.smartcare.hms.exception.AppointmentConflictException;
import com.smartcare.hms.exception.InvalidInputException;
import com.smartcare.hms.exception.ResourceNotFoundException;
import com.smartcare.hms.repository.AppointmentRepository;
import com.smartcare.hms.repository.DoctorRepository;
import com.smartcare.hms.repository.PatientRepository;
import com.smartcare.hms.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    // Repositories තුනම Constructor Injection හරහා ලබා ගැනීම
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  PatientRepository patientRepository,
                                  DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Appointment bookAppointment(Appointment appointment) {
        // Frontend එකෙන් එන ID මඟින් Database එකෙන් සම්පූර්ණ Patient සහ Doctor ලබා ගැනීම
        resolveAndAttachPatientAndDoctor(appointment);

        validate(appointment);
        checkForClash(appointment, null);
        appointment.setAppointmentStatus(Appointment.AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment updated) {
        Appointment existing = getAppointmentById(id);

        resolveAndAttachPatientAndDoctor(updated);

        validate(updated);
        checkForClash(updated, id);

        existing.setPatient(updated.getPatient());
        existing.setDoctor(updated.getDoctor());
        existing.setAppointmentDate(updated.getAppointmentDate());
        existing.setAppointmentTime(updated.getAppointmentTime());
        existing.setConsultationRoom(updated.getConsultationRoom());
        return appointmentRepository.save(existing);
    }

    @Override
    public void cancelAppointment(Long id) {
        Appointment existing = getAppointmentById(id);
        existing.setAppointmentStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(existing);
    }

    @Override
    public List<Appointment> getScheduleForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }

    // Helper Method: ID එක පදනම් කරගෙන Database එකෙන් Patient සහ Doctor ගෙන Appointment එකට සම්බන්ධ කිරීම
    private void resolveAndAttachPatientAndDoctor(Appointment appointment) {
        if (appointment.getPatient() == null || appointment.getPatient().getPatientId() == null) {
            throw new InvalidInputException("Patient ID is required");
        }
        if (appointment.getDoctor() == null || appointment.getDoctor().getDoctorId() == null) {
            throw new InvalidInputException("Doctor ID is required");
        }

        Long patientId = appointment.getPatient().getPatientId();
        Long doctorId = appointment.getDoctor().getDoctorId();

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
    }

    private void validate(Appointment appointment) {
        if (appointment.getAppointmentDate() == null) {
            throw new InvalidInputException("Appointment date is required");
        }
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new InvalidInputException("Appointment date cannot be in the past");
        }
        if (appointment.getPatient() == null || appointment.getDoctor() == null) {
            throw new InvalidInputException("Both patient and doctor must be specified");
        }
    }

    private void checkForClash(Appointment appointment, Long excludeAppointmentId) {
        List<Appointment> clashes = appointmentRepository
                .findByDoctor_DoctorIdAndAppointmentDateAndAppointmentTime(
                        appointment.getDoctor().getDoctorId(),
                        appointment.getAppointmentDate(),
                        appointment.getAppointmentTime());

        boolean hasClash = clashes.stream()
                .filter(a -> excludeAppointmentId == null || !a.getAppointmentId().equals(excludeAppointmentId))
                .anyMatch(a -> a.getAppointmentStatus() != Appointment.AppointmentStatus.CANCELLED);

        if (hasClash) {
            throw new AppointmentConflictException(
                    "Doctor already has an appointment at " + appointment.getAppointmentTime()
                            + " on " + appointment.getAppointmentDate());
        }
    }
}