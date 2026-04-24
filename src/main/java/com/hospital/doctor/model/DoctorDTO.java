package com.hospital.doctor.model;
public class DoctorDTO {
    private String name, specialization, email, phone;
    private boolean availabilityStatus;
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public String getSpecialization(){return specialization;} public void setSpecialization(String s){this.specialization=s;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getPhone(){return phone;} public void setPhone(String p){this.phone=p;}
    public boolean isAvailabilityStatus(){return availabilityStatus;} public void setAvailabilityStatus(boolean a){this.availabilityStatus=a;}
}
