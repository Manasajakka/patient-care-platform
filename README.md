# AI-Assisted Patient Care Platform — Backend

A secure, full-stack healthcare scheduling and engagement platform built for small medical clinics — designed in consultation with a practicing physician to replace fragmented tools (spreadsheets, messaging apps, separate reminder services) with one unified system.

**Frontend repo:** [patient-care-frontend](https://github.com/Manasajakka/patient-care-frontend)

## Features

- 🔐 Role-based authentication (Patient / Doctor / Admin) with BCrypt password hashing
- 📅 Recurring weekly doctor availability + live open-slot calculation
- ✅ Database-enforced double-booking prevention (unique constraint, not just app-level checks)
- 💊 Prescription records linked to appointments
- ✉️ Automated email appointment reminders (scheduled hourly job)
- 💳 Real Stripe payment processing (PaymentIntents + embedded card element)
- 🤖 AI assistant for medication/condition Q&A (Groq API, Llama 3.3 70B)
- 📊 Admin reporting by custom date range + user management

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 (Spring Web, Spring Security, Spring Data JPA) |
| ORM | Hibernate |
| Database | MySQL 9 |
| Payments | Stripe API |
| Email | Spring Mail (Gmail SMTP) |
| AI | Groq API (Llama 3.3 70B) |
| Build Tool | Maven |

## Architecture

Three-tier design — the frontend never touches the database directly; every request passes through this REST API, which enforces security and business rules before reaching MySQL or any third-party service.

## Running Locally

1. Clone the repo
2. Create a MySQL database named `patient_care_db`
3. Configure `src/main/resources/application.properties` with your database credentials and API keys
4. Run `mvn clean install`
5. Run the application via your IDE or `mvn spring-boot:run`
6. API available at `http://localhost:8080`

## Key Design Decisions

- **Doctor/Patient separated from User** — avoids empty, wasted columns for role-specific data (normalization)
- **Double-booking prevention at the database level** — a `UNIQUE(doctor_id, appointment_date, appointment_time)` constraint guarantees data integrity even under concurrent requests
- **Payment security** — the backend only creates Stripe PaymentIntents; raw card numbers never touch this server