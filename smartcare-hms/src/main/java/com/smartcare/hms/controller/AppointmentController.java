package com.smartcare.hms.controller;

import com.smartcare.hms.entity.Appointment;
import com.smartcare.hms.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> book(@Valid @RequestBody Appointment appointment) {
        return new ResponseEntity<>(appointmentService.bookAppointment(appointment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.getAllAppointments();
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @Valid @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}/schedule")
    public List<Appointment> doctorSchedule(@PathVariable Long doctorId) {
        return appointmentService.getScheduleForDoctor(doctorId);
    }
}
