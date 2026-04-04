<h1 align="center">Zorvyn — Finance Dashboard API</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring%20Security-JWT-blue?style=flat-square&logo=springsecurity" alt="Spring Security" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-336791?style=flat-square&logo=postgresql" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=flat-square&logo=swagger" alt="Swagger" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=flat-square" alt="MIT License" />
</p>

<p align="center">
  A role-based financial management REST API built with Spring Boot, secured with JWT authentication, and backed by PostgreSQL. Supports multi-role access control for managing transactions and viewing dashboard analytics.
</p>

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture Overview](#architecture-overview)
- [Roles and Permissions](#roles-and-permissions)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Dashboard Response](#dashboard-response)
- [Project Structure](#project-structure)
- [Design Decisions](#design-decisions)
- [How to Test](#how-to-test)

---

## Features

- **JWT-based Authentication** — Stateless, token-based login with 24-hour expiry
- **Role-Based Access Control** — Three distinct roles: `ADMIN`, `ANALYST`, `VIEWER`
- **Transaction Management** — Full CRUD with filtering by category, type, and date
- **Dashboard Analytics** — Aggregated income, expense, net balance, and category breakdowns
- **User Lifecycle Management** — Soft-delete via `isActive` flag; admins can activate/deactivate users
- **Swagger UI** — Interactive API documentation available out of the box
- **Clean Architecture** — Layered separation: Controller → Service → Repository → DB

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot 3.x | Application framework |
| Spring Security | Authentication & authorization |
| JWT (jjwt) | Stateless token management |
| Spring Data JPA | ORM & database access |
| PostgreSQL | Relational database |
| SpringDoc OpenAPI | Swagger UI & API docs |
| Lombok | Boilerplate reduction |
| Maven | Build & dependency management |

---

## Architecture Overview

```
Client Request
     │
     ▼
 JWT Filter (validates token)
     │
     ▼
 Spring Security (checks role)
     │
     ▼
 Controller → Service → Repository → PostgreSQL
     │
     ▼
 Response DTO (structured JSON)
```

---

## Roles and Permissions

| Role | Transactions | Users | Dashboard |
|---|---|---|---|
| `ADMIN` | Full CRUD | Full access | Full view |
| `ANALYST` | View all + Create | No access | Full view |
| `VIEWER` | View own only | No access | Own data only |

> **Note:** Email is used as the unique identifier for authentication.

---

## API Endpoints

### Auth — Public

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive a JWT token |

### Transactions — Protected

| Method | Endpoint | Role Required | Description |
|---|---|---|---|
| `POST` | `/api/transactions/` | ADMIN, ANALYST | Create a transaction |
| `GET` | `/api/transactions/` | ALL | Get all transactions |
| `GET` | `/api/transactions/{id}` | ALL | Get transaction by ID |
| `PUT` | `/api/transactions/{id}` | ADMIN | Update a transaction |
| `DELETE` | `/api/transactions/{id}` | ADMIN | Delete a transaction |
| `GET` | `/api/transactions/filter` | ALL | Filter transactions by params |

### Users — Admin Only

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/users/` | Get all users |
| `GET` | `/api/users/{id}` | Get user by ID |
| `PUT` | `/api/users/{id}/role` | Update a user's role |
| `PUT` | `/api/users/{id}/deactivate` | Deactivate a user (soft delete) |
| `PUT` | `/api/users/{id}/activate` | Reactivate a user |

### Dashboard — Protected

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard/` | Get summary analytics |

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL running locally

### 1. Clone the Repository

```bash
git clone https://github.com/parthsharma5575/zorvyn.git
cd zorvyn
```

### 2. Create the Database

```sql
CREATE DATABASE finance_dashboard;
```

### 3. Configure `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finance_dashboard
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

jwt.secret=your_base64_encoded_secret
jwt.expiration=86400000

springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

### 5. Open Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## Dashboard Response

Sample response from `GET /api/dashboard/`:

```json
{
  "totalIncome": 5000.00,
  "totalExpense": 2000.00,
  "netBalance": 3000.00,
  "categoryTotals": {
    "Salary": 5000.00,
    "Rent": 1500.00,
    "Food": 500.00
  },
  "recentTransactions": []
}
```

---

## Project Structure

```
src/main/java/com/zorvyn/
├── config/          # Security configuration, Swagger/OpenAPI setup
├── controller/      # REST controllers (Auth, Transaction, User, Dashboard)
├── dto/
│   ├── request/     # Incoming request payloads
│   └── response/    # Outgoing response models
├── exception/       # Custom exceptions & global exception handler
├── mappers/         # Entity ↔ DTO conversion
├── model/
│   └── enums/       # Role, TransactionType enums
├── repository/      # Spring Data JPA repositories
├── security/        # JWT filter, UserDetailsService
├── service/         # Business logic layer
└── util/            # JWT utility class
```

---

## Design Decisions(Assumption)

- **Email as username** — Simplifies authentication without a separate username field
- **Soft delete for users** — Users are deactivated via `isActive` flag rather than removed from the DB, preserving audit trails
- **ANALYST can create but not modify/delete** — Follows the principle of least privilege for analyst-level access
- **VIEWER sees only own data** — Transactions and dashboard scoped to the authenticated user's records
- **`LocalDate` for transaction dates** — Captures the business date of the transaction, not the system timestamp
- **`createdAt` is immutable** — Auto-set on record creation; never modified on updates
- **JWT expiry set to 24 hours** — Balances security and usability for session management

---

## How to Test

1. Register a user via `POST /api/auth/register`
2. Login via `POST /api/auth/login` to receive your JWT token
3. Open [Swagger UI](http://localhost:8080/swagger-ui/index.html)
4. Click **Authorize** and enter: `Bearer <your_token>`
5. All protected endpoints are now accessible based on your role

---

<p align="center">Built with Spring Boot &bull; Secured with JWT &bull; Powered by PostgreSQL</p>
