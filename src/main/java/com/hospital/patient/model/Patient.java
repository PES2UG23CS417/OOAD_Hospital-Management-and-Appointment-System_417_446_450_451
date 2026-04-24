package com.hospital.patient.model;
import jakarta.persistence.*;

@Entity @Table(name = "patients")
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    private String name;
    @Column(unique = true) private String email;
    private String password;
    private String phone;
    private int age;
    private String gender;
    @Column(columnDefinition = "TEXT") private String medicalHistory;

    public Patient() {}
    public Patient(String name,String email,String password,String phone,int age,String gender){
        this.name=name;this.email=email;this.password=password;
        this.phone=phone;this.age=age;this.gender=gender;
    }
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
    public String getPhone(){return phone;} public void setPhone(String p){this.phone=p;}
    public int getAge(){return age;} public void setAge(int a){this.age=a;}
    public String getGender(){return gender;} public void setGender(String g){this.gender=g;}
    public String getMedicalHistory(){return medicalHistory;} public void setMedicalHistory(String m){this.medicalHistory=m;}
}
