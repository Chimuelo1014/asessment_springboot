# CoopCredit - Credit Application Management System

## 📋 Project Overview

CoopCredit is a comprehensive credit application management system built with **Hexagonal Architecture** (Ports & Adapters), designed to digitalize and automate the credit request and evaluation process for a savings and credit cooperative.

### Key Features
- ✅ Hexagonal Architecture (Clean Architecture)
- ✅ Microservices Architecture
- ✅ JWT Security with Role-Based Access Control
- ✅ External Risk Evaluation Service Integration
- ✅ RESTful API with OpenAPI documentation
- ✅ Observability with Actuator, Micrometer & Prometheus
- ✅ Comprehensive Testing (Unit, Integration, Testcontainers)
- ✅ Dockerized Deployment
- ✅ Circuit Breaker & Retry with Resilience4j
- ✅ Distributed Caching with Redis
- ✅ Analytics Dashboard (React + Recharts)
- ✅ Database Migrations with Flyway

---

## 🏗️ Architecture

### Hexagonal Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                      │
│  ┌─────────────────────┐         ┌─────────────────────┐   │
│  │  Input Adapters     │         │  Output Adapters    │   │
│  │  - REST Controllers │         │  - JPA Repositories │   │
│  │  - DTOs            │         │  - External REST    │   │
│  │  - Security        │         │  - Mappers         │   │
│  └─────────┬───────────┘         └─────────┬───────────┘   │
└────────────┼─────────────────────────────────┼──────────────┘
             │                                 │
             │ ┌───────────────────────────┐   │
             ├─┤   APPLICATION LAYER      │───┤
             │ │   - Use Cases (Pure)    │   │
             │ │   - Business Logic      │   │
             │ └───────────┬───────────────┘   │
             │             │                   │
             │ ┌───────────▼───────────────┐   │
             └─┤      DOMAIN LAYER         │───┘
               │   - Entities (Pure POJOs)│
               │   - Domain Logic         │
               │   - Ports (Interfaces)   │
               │   - Exceptions           │
               └──────────────────────────┘
```

### Microservices Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                         Client Layer                          │
│                    (Postman / Frontend)                       │
└────────────────────────────┬─────────────────────────────────┘
                             │
                ┌────────────▼────────────┐
                │   credit-application    │
                │        -service         │
                │  ┌──────────────────┐  │
                │  │  REST API        │  │
                │  │  - /auth         │  │
                │  │  - /affiliates   │  │
                │  │  - /applications │  │
                │  └────────┬─────────┘  │
                │           │             │
                │  ┌────────▼─────────┐  │
                │  │  Use Cases       │  │
                │  │  - Register      │  │
                │  │  - Evaluate      │  │
                │  └────────┬─────────┘  │
                │           │             │
                │  ┌────────▼─────────┐  │
                │  │  Adapters        │  │
                │  │  - JPA           │  │
                │  │  - REST Client   │──┼────┐
                │  │  - Redis Cache   │  │    │
                │  └────────┬─────────┘  │    │
                └─────────┬───────────────┘    │
                          │                    │
                ┌─────────▼──────────┐  ┌──────▼──────────┐
                │   PostgreSQL DB    │  │  risk-central   │
                │   - Affiliates     │  │  -mock-service  │
                │   - Applications   │  │  - Score Calc   │
                │   - Risk Evals     │  │  - Deterministic│
                └────────────────────┘  └─────────────────┘
                          │
                ┌─────────▼──────────┐
                │      Redis         │
                │   - Distributed    │
                │     Cache          │
                └────────────────────┘
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker & Docker Compose**
- **PostgreSQL 16** (optional, included in docker-compose)
- **Postman** or **Insomnia** for API testing

### Running with Docker Compose (Recommended)

1. **Clone the repository**
```bash
git clone <repository-url>
cd simulacro-prueba-springboot
```

2. **Build and start all services**
```bash
docker-compose up --build
```

3. **Verify services are running**
```bash
# Credit Application Service
curl http://localhost:8080/actuator/health

# Risk Central Mock Service
curl http://localhost:8081/actuator/health

# Frontend (Analytics Dashboard)
# Open http://localhost:5173 in your browser
```

4. **Access the services**
- Credit Application Service: `http://localhost:8080`
- Risk Central Mock Service: `http://localhost:8081`
- PostgreSQL: `localhost:5432`
- Prometheus Metrics: 
  - `http://localhost:8080/actuator/prometheus`
  - `http://localhost:8081/actuator/prometheus`

### Running Locally (Development)

1. **Start PostgreSQL**
```bash
docker run -d \
  --name postgres-coopcredit \
  -e POSTGRES_DB=coopcredit_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16-alpine
```

2. **Start Risk Central Mock Service**
```bash
cd risk-central-mock-service
./mvnw spring-boot:run
```

3. **Start Credit Application Service**
```bash
cd credit-application-service
./mvnw spring-boot:run
```

---

## 🔐 Security & Authentication

### User Roles

| Role | Description | Access Level |
|------|-------------|--------------|
| `ROLE_AFILIADO` | Affiliate user | Can create and view own credit applications |
| `ROLE_ANALISTA` | Analyst user | Can view all applications, approve/reject, create affiliates |
| `ROLE_ADMIN` | Administrator | Full access to all resources |

### Default Users (for testing)

```json
// Admin User
{
  "username": "admin",
  "password": "password",
  "role": "ROLE_ADMIN"
}

// Analyst User
{
  "username": "analyst",
  "password": "password",
  "role": "ROLE_ANALISTA"
}
```

### Authentication Flow

1. **Register a new user** (or use default users)
```bash
POST /api/v1/auth/register
{
  "username": "john_doe",
  "password": "securePassword123",
  "email": "john@example.com",
  "role": "ROLE_AFILIADO"
}
```

2. **Login to get JWT token**
```bash
POST /api/v1/auth/login
{
  "username": "john_doe",
  "password": "securePassword123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "role": "ROLE_AFILIADO"
}
```

3. **Use the token in subsequent requests**
```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### ⚠️ Role Assignment Security

> **Production-Ready Implementation**
> 
> All new user registrations via the frontend are assigned the `ROLE_AFILIADO` role by default. This is the proper security implementation for production environments.
> 
> **Role Assignment for Elevated Privileges:**
> - `ROLE_ANALISTA` and `ROLE_ADMIN` roles can only be assigned by administrators
> - Default test users (admin, analyst) are pre-configured in the database via Flyway migrations
> - In production, an administrative endpoint would be provided for role management
>
> This follows security best practices where users cannot self-assign privileged roles.

---

## 📡 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/auth/register` | Register new user | Public |
| POST | `/api/v1/auth/login` | Login and get JWT | Public |

### Affiliate Endpoints

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/affiliates` | Create affiliate | ANALISTA, ADMIN |
| GET | `/api/v1/affiliates` | Get all affiliates | ANALISTA, ADMIN |
| GET | `/api/v1/affiliates/{id}` | Get affiliate by ID | AFILIADO, ANALISTA, ADMIN |
| GET | `/api/v1/affiliates/document/{document}` | Get affiliate by document | AFILIADO, ANALISTA, ADMIN |

### Credit Application Endpoints

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/credit-applications` | Create application | AFILIADO |
| GET | `/api/v1/credit-applications` | Get all applications | ANALISTA, ADMIN |
| GET | `/api/v1/credit-applications/{id}` | Get application by ID | ANALISTA, ADMIN |
| GET | `/api/v1/credit-applications/pending` | Get pending applications | ANALISTA, ADMIN |
| GET | `/api/v1/credit-applications/my-applications` | Get own applications | AFILIADO |
| POST | `/api/v1/credit-applications/{id}/evaluate` | Evaluate application | ANALISTA, ADMIN |
| POST | `/api/v1/credit-applications/{id}/approve` | Manually approve | ANALISTA, ADMIN |
| POST | `/api/v1/credit-applications/{id}/reject` | Manually reject | ANALISTA, ADMIN |

### Analytics Endpoints

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/analytics/approval-rate` | Get approval rates over time | ADMIN |
| GET | `/api/v1/analytics/amount-by-status` | Get total amounts per status | ADMIN |
| GET | `/api/v1/analytics/applications-per-month` | Get application volume per month | ADMIN |

### Risk Evaluation Endpoint (Mock Service)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/risk-evaluation` | Evaluate credit risk |

---

## 🎯 Business Rules

### Affiliate Requirements
- ✅ Unique document number
- ✅ Monthly salary > 0
- ✅ Must be ACTIVE status to request credit
- ✅ Minimum seniority: **6 months**

### Credit Application Rules
- ✅ Maximum credit amount: **5x monthly salary**
- ✅ Maximum debt ratio: **40% of monthly income**
- ✅ Term: minimum 1 month
- ✅ Automatic evaluation based on risk score

### Risk Evaluation Logic
- **Score Range**: 300-850 (deterministic based on document hash)
- **Risk Levels**:
  - `LOW`: Score ≥ 700 → Auto-approve
  - `MEDIUM`: Score 550-699 → Manual review required
  - `HIGH`: Score < 550 → Auto-reject

---

## 📊 Database Schema

### Tables

**users**
```sql
id BIGSERIAL PRIMARY KEY
username VARCHAR(50) UNIQUE NOT NULL
password VARCHAR(255) NOT NULL
email VARCHAR(100) NOT NULL
role VARCHAR(20) NOT NULL
enabled BOOLEAN DEFAULT TRUE
created_at TIMESTAMP NOT NULL
```

**affiliates**
```sql
id BIGSERIAL PRIMARY KEY
document VARCHAR(20) UNIQUE NOT NULL
full_name VARCHAR(200) NOT NULL
email VARCHAR(100) NOT NULL
phone VARCHAR(20)
monthly_salary DECIMAL(19,2) NOT NULL
status VARCHAR(20) NOT NULL
affiliation_date DATE NOT NULL
version BIGINT
```

**credit_applications**
```sql
id BIGSERIAL PRIMARY KEY
affiliate_id BIGINT NOT NULL (FK)
requested_amount DECIMAL(19,2) NOT NULL
term_months INTEGER NOT NULL
status VARCHAR(20) NOT NULL
application_date TIMESTAMP NOT NULL
evaluation_date TIMESTAMP
analyst_comments TEXT
version BIGINT
```

**risk_evaluations**
```sql
id BIGSERIAL PRIMARY KEY
credit_application_id BIGINT UNIQUE NOT NULL (FK)
score INTEGER NOT NULL
risk_level VARCHAR(20) NOT NULL
recommendation VARCHAR(50)
evaluation_message TEXT
evaluation_date TIMESTAMP NOT NULL
```

---

## 🧪 Testing

### Running Tests

**Unit Tests**
```bash
cd credit-application-service
./mvnw test
```

**Integration Tests with Testcontainers**
```bash
./mvnw verify
```

### Test Coverage

- ✅ Unit tests for pure domain logic
- ✅ Unit tests for use cases with mocks
- ✅ Integration tests with MockMvc
- ✅ Integration tests with Testcontainers (PostgreSQL)
- ✅ Security tests

---

## 📈 Observability & Metrics

### Actuator Endpoints

```bash
# Health check
GET http://localhost:8080/actuator/health

# Metrics
GET http://localhost:8080/actuator/metrics

# Prometheus metrics
GET http://localhost:8080/actuator/prometheus
```

### Custom Metrics

- `credit_applications_created_total` - Counter of created applications
- `credit_applications_evaluated_total` - Counter of evaluated applications
- `risk_evaluation_calls_total` - Counter of risk service calls
- `risk_evaluation_duration` - Timer for risk evaluation duration
- `risk_evaluations_total` (Mock Service) - Counter by risk level

---

## 🐛 Error Handling

The API uses **RFC 7807 Problem Details** for standardized error responses.

### Error Response Format
```json
{
  "type": "https://example.com/errors/not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Affiliate not found with id: 123",
  "timestamp": "2024-12-09T10:30:00Z"
}
```

### HTTP Status Codes
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Validation error or business rule violation
- `401 Unauthorized` - Missing or invalid JWT
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Server error

---

## 📦 Project Structure

```
simulacro-prueba-springboot/
├── credit-application-service/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/.../
│   │   │   │   ├── domain/              # Pure domain
│   │   │   │   │   ├── model/           # Entities (Pure POJOs)
│   │   │   │   │   ├── port/            # Interfaces
│   │   │   │   │   │   ├── in/          # Use case interfaces
│   │   │   │   │   │   └── out/         # Repository interfaces
│   │   │   │   │   └── exception/       # Domain exceptions
│   │   │   │   ├── application/         # Use cases
│   │   │   │   │   └── usecase/         # Pure business logic
│   │   │   │   └── infrastructure/      # Framework code
│   │   │   │       ├── adapter/
│   │   │   │       │   ├── in/          # Controllers, DTOs
│   │   │   │       │   └── out/         # JPA, REST clients
│   │   │   │       └── config/          # Spring configuration
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/migration/        # Flyway scripts
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── risk-central-mock-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── docker-compose.yml
├── postman/
│   └── CoopCredit_Collection.json
└── README.md
```

---

## 🔧 Configuration

### Environment Variables

**Credit Application Service**
```properties
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=coopcredit_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# External Services
RISK_SERVICE_HOST=localhost
RISK_SERVICE_PORT=8081

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

---

## 📝 Example Usage

### Complete Flow Example

1. **Register Analyst User**
```bash
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "username": "analyst1",
  "password": "password123",
  "email": "analyst1@coopcredit.com",
  "role": "ROLE_ANALISTA"
}
```

2. **Login as Analyst**
```bash
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
  "username": "analyst1",
  "password": "password123"
}

# Save the token from response
```

3. **Create Affiliate**
```bash
POST http://localhost:8080/api/v1/affiliates
Authorization: Bearer {token}
Content-Type: application/json

{
  "document": "1234567890",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "phone": "555-1234",
  "monthlySalary": 5000.00
}
```

4. **Register Affiliate User**
```bash
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123",
  "email": "john.doe@example.com",
  "role": "ROLE_AFILIADO"
}
```

5. **Login as Affiliate and Create Application**
```bash
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}

# Then create application
POST http://localhost:8080/api/v1/credit-applications
Authorization: Bearer {affiliate_token}
Content-Type: application/json

{
  "affiliateId": 1,
  "requestedAmount": 15000.00,
  "termMonths": 24
}
```

6. **Evaluate Application (as Analyst)**
```bash
POST http://localhost:8080/api/v1/credit-applications/1/evaluate
Authorization: Bearer {analyst_token}
```

---

## 👥 Authors

- **Student**: Samuel Quintero Sanchez
- **Program**: Riwi Backend Development
- **Module**: Spring Boot


## 🎓 Learning Objectives Achieved

- ✅ Hexagonal Architecture implementation
- ✅ Microservices communication
- ✅ JWT Security & Role-Based Access Control
- ✅ JPA Advanced (EntityGraph, join fetch, optimizations)
- ✅ RESTful API design
- ✅ Error handling with RFC 7807
- ✅ Observability with Actuator & Micrometer
- ✅ Testing (Unit, Integration, Testcontainers)
- ✅ Docker & Docker Compose
- ✅ Database migrations with Flyway
- ✅ Clean Code & SOLID principles