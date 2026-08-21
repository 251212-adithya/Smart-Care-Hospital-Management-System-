package com.smartcare.hms.exception;

/** Thrown when a doctor already has an appointment at the requested date/time. */
public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(String message) {
        super(message);
    }
}
