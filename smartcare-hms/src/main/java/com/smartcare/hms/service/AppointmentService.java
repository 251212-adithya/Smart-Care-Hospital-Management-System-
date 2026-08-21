package com.smartcare.hms.service;

import com.smartcare.hms.entity.Appointment;
import java.util.List;

public interface AppointmentService {
    Appointment bookAppointment(Appointment appointment);
    Appointment getAppointmentById(Long id);
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Long id, Appointment appointment);
    void cancelAppointment(Long id);
    List<Appointment> getScheduleForDoctor(Long doctorId);
}
