package com.hospital;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hospital Management System — Version 1 (Basic MVC, No Design Patterns)
 *
 * Architecture: Spring Boot MVC + MySQL + Thymeleaf
 * Role-Based Access: Hospital Staff, Patient, Finance Admin
 *
 * Modules:
 *   - Patient Management (Member 1)
 *   - Doctor Management  (Member 2)
 *   - Appointment System (Member 3)
 *   - Billing System     (Member 4)
 *
 * Course: UE23CS352B — Object Oriented Analysis & Design
 */
@SpringBootApplication
public class HospitalManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }
}
