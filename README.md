# Hospital Management System V1

A Spring Boot MVC application for managing hospital operations including patient registration, doctor management, appointments, and billing.

##  Project Overview

- **Project Name**: Hospital Management System
- **Version**: 1.0
- **Course**: UE23CS352B — Object Oriented Analysis and Design
- **Architecture**: Spring Boot MVC + MySQL + Thymeleaf
- **Java Version**: 17

##  Technology Stack

| Component | Technology |
|-----------|------------|
| Backend | Spring Boot 3.2.0 |
| Frontend | Thymeleaf + HTML/CSS |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Build Tool | Maven |

##  Project Structure

```
src/main/
├── java/com/hospital/
│   ├── HospitalManagementApplication.java
│   ├── appointment/          # Appointment Module
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── billing/              # Billing Module
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── config/               # Configuration
│   ├── controller/           # Home & Login
│   ├── doctor/               # Doctor Module
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── patient/              # Patient Module
│       ├── controller/
│       ├── model/
│       ├── repository/
│       └── service/
└── resources/
    ├── application.properties
    └── templates/
        ├── home.html
        ├── login.html
        ├── appointments/
        ├── billing/
        ├── doctors/
        ├── fragments/
        └── patients/
```

##  Getting Started

### Prerequisites

- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Database Setup

```sql
CREATE DATABASE hospital_db;
```

Update credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YourPassword
```

### Build & Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080`

##  Features

### Patient Module
- Patient registration and login
- View and book appointments
- View appointment history
- Access billing information

### Doctor Module
- Add new doctors
- View doctor list
- Filter doctors by specialization
- Toggle doctor availability
- Delete doctors

### Appointment Module
- Book appointments
- View patient/staff dashboards
- Reschedule appointments
- Cancel appointments
- Automatic slot management

### Billing Module
- Generate bills for appointments
- Apply discounts (Senior Citizen, Insurance, etc.)
- View invoice history
- Invoice detail tracking

##  Design Patterns & Principles

### Design Patterns
- **Factory Pattern**: Appointment and Doctor creation
- **Strategy Pattern**: Scheduling algorithms, discount calculations
- **Observer Pattern**: Appointment event notifications
- **Singleton Pattern**: Spring beans (default scope)

### Design Principles
- **SRP**: Single Responsibility Principle
- **OCP**: Open/Closed Principle
- **DIP**: Dependency Inversion Principle
- **Low Coupling**: Layered architecture
- **High Cohesion**: Focused class responsibilities

##  Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Patient | patient1 | password |
| Staff | staff1 | password |

##  Build Output

After building, the JAR file is located at:
```
target/hospital-management-0.0.1-SNAPSHOT.jar
```

##  License

This project is developed for educational purposes.
