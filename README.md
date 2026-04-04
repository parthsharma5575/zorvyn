# Finance Dashboard API

A role-based financial management backend built with Spring Boot , Spring Security with JWT authentication, and PostgreSQL. Designed to manage financial transactions and provide dashboard analytics based on user roles.

---

## Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring Security + JWT (jjwt 0.12.6)
- PostgreSQL
- SpringDoc OpenAPI (Swagger UI)
- Lombok

---

## Roles and Permissions

| Role    | Transactions         | Users         | Dashboard |
|---------|----------------------|---------------|-----------|
| ADMIN   | Full CRUD            | Full access   | Full view |
| ANALYST | View all, Create     | No access     | Full view |
| VIEWER  | View own only        | No access     | Own data  |

---

## Setup Instructions

### 1. Prerequisites
- Java 21
- Maven
- PostgreSQL running locally

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE finance_dashboard;
```

### 3. Configure application.properties
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

### 5. Access Swagger UI
http://localhost:8080/swagger-ui/index.html

---

## API Endpoints

### Auth — Public
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register a new user |
| POST | /api/auth/login | Login and receive JWT token |

### Transactions — Protected
| Method | Endpoint | Role Required | Description |
|--------|----------|---------------|-------------|
| POST | /api/transactions/ | ADMIN, ANALYST | Create transaction |
| GET | /api/transactions/ | ALL | Get transactions |
| GET | /api/transactions/{id} | ALL | Get transaction by ID |
| PUT | /api/transactions/{id} | ADMIN | Update transaction |
| DELETE | /api/transactions/{id} | ADMIN | Delete transaction |
| GET | /api/transactions/filter | ALL | Filter transactions |

### Users — Admin Only
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/users/ | Get all users |
| GET | /api/users/{id} | Get user by ID |
| PUT | /api/users/{id}/role | Update user role |
| PUT | /api/users/{id}/deactivate | Deactivate user |
| PUT | /api/users/{id}/activate | Activate user |

### Dashboard — Protected
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/dashboard/ | Get summary analytics |

---

## Dashboard Summary Response
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

## Assumptions Made

- Email is used as the username for authentication
- ANALYST can create transactions but cannot modify or delete
- VIEWER can only view their own transactions and dashboard data
- Soft delete is used for users via `isActive` flag — users are deactivated, not removed
- JWT tokens expire after 24 hours
- Transaction dates use `LocalDate` — the date the transaction occurred
- `createdAt` is auto-set on record creation and never modified

---

## Project Structure
com.zorvyn
├── config          → Security and Swagger configuration
├── controller      → REST controllers
├── dto
│   ├── request     → Incoming request DTOs
│   └── response    → Outgoing response DTOs
├── exception       → Custom exceptions and global handler
├── mappers         → Entity to DTO conversion
├── model
│   └── enums       → Role and TransactionType enums
├── repository      → JPA repositories
├── security        → JWT filter and UserDetailsService
├── service         → Business logic
└── util            → JWT utility

---

## How to Test

1. Register a user via `/api/auth/register`
2. Login via `/api/auth/login` to receive JWT token
3. Click **Authorize** in Swagger UI and enter `Bearer <your_token>`
4. All protected endpoints are now accessible based on your role