package com.hospital.appointment.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "appointment_slots")
public class AppointmentSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Integer doctorId;
    private String doctorName;
    private String doctorSpecialty;
    private LocalDateTime slotTime;
    private boolean booked = false;

    public AppointmentSlot() {}

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Integer getDoctorId(){return doctorId;} public void setDoctorId(Integer d){this.doctorId=d;}
    public String getDoctorName(){return doctorName;} public void setDoctorName(String n){this.doctorName=n;}
    public String getDoctorSpecialty(){return doctorSpecialty;} public void setDoctorSpecialty(String s){this.doctorSpecialty=s;}
    public LocalDateTime getSlotTime(){return slotTime;} public void setSlotTime(LocalDateTime t){this.slotTime=t;}
    public boolean isBooked(){return booked;} public void setBooked(boolean b){this.booked=b;}
}
