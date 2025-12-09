# Credit Application Service

## Overview

The **Credit Application Service** is the core microservice of the CoopCredit system, responsible for managing credit applications, affiliates, and coordinating with external risk evaluation services.

## Architecture

This service implements **Hexagonal Architecture (Ports & Adapters)** with clear separation of concerns:

```
├── domain/                  # Pure business logic (no framework dependencies)
│   ├── model/              # Domain entities (POJOs)
│   ├── port/in/            # Use case interfaces
│   ├── port/out/           # Repository/external service interfaces
│   └── exception/          # Domain-specific exceptions
├── application/            # Application layer
│   └── usecase/           # Use case implementations
└── infrastructure/         # Framework-specific code
    ├── adapter/           
    │   ├── in/web/        # REST controllers, DTOs
    │   └── out/           # JPA repositories, REST clients
    └── config/            # Spring configuration
```

## Features

- ✅ **Affiliate Management**: CRUD operations for credit cooperative members
- ✅ **Credit Application Processing**: Complete workflow from submission to approval/rejection
- ✅ **Risk Evaluation Integration**: Communicates with external risk assessment service
- ✅ **JWT Authentication**: Stateless security with role-based access control
- ✅ **Transaction Management**: Ensures data consistency
- ✅ **Observability**: Metrics, health checks, and structured logging
- ✅ **N+1 Query Prevention**: Optimized database access with EntityGraph

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.8**
- **PostgreSQL 16**
- **Spring Security + JWT**
- **Spring Data JPA + Hibernate**
- **Flyway** (Database migrations)
- **Micrometer + Prometheus** (Metrics)
- **Spring Boot Actuator** (Observability)
- **JUnit 5 + Mockito** (Testing)
- **Testcontainers** (Integration testing)

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- PostgreSQL 16 (or use Docker Compose)

## Running Locally

### 1. Start PostgreSQL

```bash
docker run -d \
  --name postgres-coopcredit \
  -e POSTGRES_DB=coopcredit_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16-alpine
```

### 2. Start the Service

```bash
cd credit-application-service
./mvnw spring-boot:run
```

The service will be available at `http://localhost:8080`

## Configuration

Key configuration properties (see `application.properties`):

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/coopcredit_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=<your-secret-key>
jwt.expiration=86400000

# External Services
external.risk-service.url=http://localhost:8081

# Actuator
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/auth/register` | Register new user | No |
| POST | `/api/v1/auth/login` | Login and get JWT token | No |

### Affiliates

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/v1/affiliates` | Create affiliate | ANALISTA, ADMIN |
| GET | `/api/v1/affiliates` | List all affiliates | ANALISTA, ADMIN |
| GET | `/api/v1/affiliates/{id}` | Get affiliate by ID | AFILIADO, ANALISTA, ADMIN |
| GET | `/api/v1/affiliates/document/{document}` | Get by document | AFILIADO, ANALISTA, ADMIN |
| PUT | `/api/v1/affiliates/{id}` | Update affiliate | ANALISTA, ADMIN |

### Credit Applications

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| POST | `/api/v1/credit-applications` | Create application | AFILIADO |
| GET | `/api/v1/credit-applications` | List all applications | ANALISTA, ADMIN |
| GET | `/api/v1/credit-applications/{id}` | Get application by ID | ANALISTA, ADMIN |
| GET | `/api/v1/credit-applications/pending` | Get pending applications | ANALISTA, ADMIN |
| GET | `/api/v1/credit-applications/my-applications?affiliateId={id}` | Get own applications | AFILIADO |
| POST | `/api/v1/credit-applications/{id}/evaluate` | Evaluate application | ANALISTA, ADMIN |
| POST | `/api/v1/credit-applications/{id}/approve` | Manually approve | ANALISTA, ADMIN |
| POST | `/api/v1/credit-applications/{id}/reject` | Manually reject | ANALISTA, ADMIN |

## Business Rules

### Affiliate Requirements
- ✅ Unique document number
- ✅ Monthly salary > 0
- ✅ Must be ACTIVE status to request credit
- ✅ Minimum seniority: 6 months

### Credit Application Rules
- ✅ Maximum credit amount: 5x monthly salary
- ✅ Maximum debt ratio: 40% of monthly income
- ✅ Minimum term: 1 month
- ✅ Automatic evaluation based on risk score

### Risk-Based Decision
- **LOW RISK** (Score ≥ 700): Auto-approve
- **MEDIUM RISK** (Score 550-699): Manual review required
- **HIGH RISK** (Score < 550): Auto-reject

## Database Schema

### Affiliates Table
```sql
CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    monthly_salary DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    affiliation_date DATE NOT NULL,
    version BIGINT
);
```

### Credit Applications Table
```sql
CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    requested_amount DECIMAL(19,2) NOT NULL,
    term_months INTEGER NOT NULL,
    interest_rate DECIMAL(5,2),
    status VARCHAR(20) NOT NULL,
    application_date TIMESTAMP NOT NULL,
    evaluation_date TIMESTAMP,
    analyst_comments TEXT,
    version BIGINT,
    FOREIGN KEY (affiliate_id) REFERENCES affiliates(id)
);
```

### Risk Evaluations Table
```sql
CREATE TABLE risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    credit_application_id BIGINT UNIQUE NOT NULL,
    score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    recommendation VARCHAR(50),
    evaluation_message TEXT,
    evaluation_date TIMESTAMP NOT NULL,
    FOREIGN KEY (credit_application_id) REFERENCES credit_applications(id)
);
```

## Testing

### Run Unit Tests
```bash
./mvnw test
```

### Run Integration Tests
```bash
./mvnw verify
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=RegisterAffiliateServiceTest
```

### Test Coverage
- ✅ Unit tests for all use cases
- ✅ Integration tests for REST controllers
- ✅ Security tests with JWT
- ✅ JPA repository tests
- ✅ Exception handler tests

## Observability

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Prometheus Metrics
```bash
curl http://localhost:8080/actuator/prometheus
```

### Custom Metrics
- `credit_applications_created_total` - Counter of created applications
- `credit_applications_evaluated_total` - Counter of evaluated applications
- `risk_evaluation_calls_total` - Counter of risk service calls
- `risk_evaluation_duration` - Timer for risk evaluation

## Error Handling

All errors follow **RFC 7807 Problem Details** standard:

```json
{
  "type": "https://example.com/errors/not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Affiliate not found with id: 123",
  "instance": "/api/v1/affiliates/123",
  "timestamp": "2024-12-09T16:00:00Z",
  "traceId": "550e8400-e29b-41d4-a716-446655440000"
}
```

## Security

### Default Test Users

Pre-configured in database via Flyway:

```
Username: admin
Password: password
Role: ROLE_ADMIN

Username: analyst  
Password: password
Role: ROLE_ANALISTA
```

### Role Permissions

- **ROLE_AFILIADO**: Can create and view own credit applications
- **ROLE_ANALISTA**: Can manage affiliates, view all applications, approve/reject
- **ROLE_ADMIN**: Full system access

## Docker

### Build Image
```bash
docker build -t credit-application-service .
```

### Run Container
```bash
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=coopcredit_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  credit-application-service
```

## API Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/api-docs`

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check connection credentials in `application.properties`
- Ensure database `coopcredit_db` exists

### JWT Token Errors
- Verify token is included in Authorization header
- Check token hasn't expired (24 hours default)
- Ensure token format: `Bearer <token>`

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

## License

© 2024 CoopCredit - Academic Project
