package com.hospital.appointment.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "appointments")
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long patientId;
    private String patientName;
    private Integer doctorId;
    private String doctorName;
    private LocalDateTime appointmentTime;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
    private String notes;

    public Appointment() {}

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getPatientId(){return patientId;} public void setPatientId(Long p){this.patientId=p;}
    public String getPatientName(){return patientName;} public void setPatientName(String n){this.patientName=n;}
    public Integer getDoctorId(){return doctorId;} public void setDoctorId(Integer d){this.doctorId=d;}
    public String getDoctorName(){return doctorName;} public void setDoctorName(String n){this.doctorName=n;}
    public LocalDateTime getAppointmentTime(){return appointmentTime;} public void setAppointmentTime(LocalDateTime t){this.appointmentTime=t;}
    public AppointmentStatus getStatus(){return status;} public void setStatus(AppointmentStatus s){this.status=s;}
    public String getNotes(){return notes;} public void setNotes(String n){this.notes=n;}
}
