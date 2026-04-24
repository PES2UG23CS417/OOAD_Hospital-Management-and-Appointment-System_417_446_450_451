package com.hospital.appointment.model;

public enum AppointmentStatus {
    SCHEDULED("Scheduled"), RESCHEDULED("Rescheduled"),
    CANCELLED("Cancelled"), COMPLETED("Completed");

    private final String displayName;
    AppointmentStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
    public boolean canBeRescheduled() { return this == SCHEDULED || this == RESCHEDULED; }
    public boolean canBeCancelled()   { return this == SCHEDULED || this == RESCHEDULED; }
}
