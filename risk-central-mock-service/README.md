# Risk Central Mock Service

## Overview

The **Risk Central Mock Service** is a lightweight microservice that simulates an external credit bureau's risk evaluation system. It provides deterministic credit scores based on applicant document numbers.

## Purpose

This service mimics a real-world credit risk evaluation service (like Equifax, TransUnion, or Experian) and is used by the Credit Application Service to assess the creditworthiness of loan applicants.

## Key Features

- ✅ **Deterministic Scoring**: Same document always returns same score
- ✅ **Consistent Results**: Uses document hash as seed for reproducibility
- ✅ **Three Risk Levels**: LOW, MEDIUM, HIGH based on score
- ✅ **Simple REST API**: Single POST endpoint
- ✅ **Lightweight**: No database, no authentication
- ✅ **Observable**: Metrics and health checks included

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.8**
- **Spring Web**
- **Micrometer + Prometheus** (Metrics)
- **Spring Boot Actuator** (Health checks)
- **OpenAPI/Swagger** (API documentation)

## How It Works

### Scoring Algorithm

1. **Input**: Receives document number, loan amount, and term
2. **Seed Generation**: Converts document to numeric seed using hash
3. **Score Calculation**: Generates score between 300-950 using seed
4. **Risk Classification**:
   - **300-500**: HIGH RISK
   - **501-700**: MEDIUM RISK
   - **701-950**: LOW RISK

### Deterministic Behavior

```java
// Same document = same score (always)
Document: "1234567890" → Score: 642 (MEDIUM)
Document: "9876543210" → Score: 823 (LOW)  
Document: "1234567890" → Score: 642 (MEDIUM) ✅ Consistent!
```

## API Endpoints

### Risk Evaluation

**Endpoint**: `POST /api/v1/risk-evaluation`

**Request**:
```json
{
  "document": "1234567890",
  "amount": 15000.00,
  "term": 24
}
```

**Response**:
```json
{
  "document": "1234567890",
  "score": 642,
  "riskLevel": "MEDIUM",
  "detail": "Moderate credit history (Score: 642). Requires additional analysis before approval."
}
```

### Health Check

**Endpoint**: `GET /api/v1/risk-evaluation/health`

**Response**:
```json
{
  "status": "Risk Central Mock Service is running"
}
```

### Actuator Health

**Endpoint**: `GET /actuator/health`

**Response**:
```json
{
  "status": "UP"
}
```

## Running Locally

### Prerequisites

- Java 21 or higher
- Maven 3.9+

### Start the Service

```bash
cd risk-central-mock-service
./mvnw spring-boot:run
```

The service will be available at `http://localhost:8081`

## Configuration

Default configuration (`application.properties`):

```properties
# Application
spring.application.name=risk-central-mock-service
server.port=8081

# Actuator
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always

# Logging
logging.level.root=INFO
logging.level.com.prueba.risk_central_mock_service=DEBUG
```

## Testing the Service

### Using cURL

```bash
# Evaluate risk for document "1234567890"
curl -X POST http://localhost:8081/api/v1/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "document": "1234567890",
    "amount": 15000.00,
    "term": 24
  }'
```

### Using Postman

1. Create POST request to `http://localhost:8081/api/v1/risk-evaluation`
2. Set header: `Content-Type: application/json`
3. Add JSON body with document, amount, and term
4. Send request and verify response

### Test Cases

| Document | Expected Score Range | Expected Risk Level |
|----------|---------------------|---------------------|
| "1234567890" | Fixed (e.g., 642) | MEDIUM |
| "9876543210" | Fixed (e.g., 823) | LOW |
| "1111111111" | Fixed (calculation based on hash) | Varies |

## Score Distribution Examples

```
Document      Hash Seed    Score    Risk Level
----------    ---------    -----    ----------
"1017654311"     varies      642      MEDIUM
"1234567890"     varies      750      LOW
"9999999999"     varies      425      HIGH
```

## Observability

### Metrics

Access Prometheus metrics at: `http://localhost:8081/actuator/prometheus`

**Custom Metrics**:
- `risk_evaluations_total{risk_level="LOW"}` - Count of LOW risk evaluations
- `risk_evaluations_total{risk_level="MEDIUM"}` - Count of MEDIUM risk evaluations  
- `risk_evaluations_total{risk_level="HIGH"}` - Count of HIGH risk evaluations

### Health Checks

```bash
# Spring Boot Actuator health
curl http://localhost:8081/actuator/health

# Custom health endpoint
curl http://localhost:8081/api/v1/risk-evaluation/health
```

## Docker

### Build Image

```bash
docker build -t risk-central-mock-service .
```

### Run Container

```bash
docker run -p 8081:8081 risk-central-mock-service
```

## API Documentation

### Swagger UI

Available at: `http://localhost:8081/swagger-ui.html`

### OpenAPI Specification

JSON format: `http://localhost:8081/api-docs`

## Integration with Credit Application Service

The Credit Application Service calls this mock service during the credit evaluation process:

```
Credit Application Service
         │
         │ HTTP POST /api/v1/risk-evaluation
         ▼
Risk Central Mock Service
         │
         │ Returns: score + risk level
         ▼
Credit Application Service
         │
         └─▶ Makes approval decision
```

## Request/Response Examples

### Example 1: Low Risk Application

**Request**:
```json
{
  "document": "1017654311",
  "amount": 10000.00,
  "term": 12
}
```

**Response**:
```json
{
  "document": "1017654311",
  "score": 750,
  "riskLevel": "LOW",
  "detail": "Excellent credit history (Score: 750). Reliable customer with low default risk."
}
```

### Example 2: High Risk Application

**Request**:
```json
{
  "document": "9999999999",
  "amount": 50000.00,
  "term": 48
}
```

**Response**:
```json
{
  "document": "9999999999",
  "score": 425,
  "riskLevel": "HIGH",
  "detail": "Poor credit history (Score: 425). High default risk, rejection recommended."
}
```

## Design Decisions

### Why Deterministic?

- ✅ **Reproducible Testing**: Same document always returns same result
- ✅ **Predictable Behavior**: Easy to test and validate
- ✅ **No Database Required**: Stateless and lightweight
- ✅ **Fast Response**: No external dependencies

### Why Hash-Based?

- ✅ **Uniqueness**: Different documents get different scores
- ✅ **Consistency**: Same document gets same score every time
- ✅ **Simplicity**: No complex algorithms or data storage needed

### Why No Authentication?

- ✅ **Simplicity**: Mock service for testing purposes
- ✅ **Internal Use**: Communicates only with Credit Application Service
- ✅ **Academic Project**: Production would require security

## Limitations

⚠️ **This is a MOCK service**. Real-world credit bureaus would:
- Require authentication/API keys
- Query actual credit history databases
- Consider payment history, debt-to-income ratio, etc.
- Return much more detailed information
- Have rate limiting and usage quotas
- Charge per query

## Troubleshooting

### Service Won't Start

```bash
# Check if port 8081 is already in use
lsof -i :8081

# Change port in application.properties if needed
server.port=8082
```

### Scores Not Consistent

- Verify you're using the exact same document number
- Check for typos or extra spaces in document field
- Review logs for any errors

## Testing

### Run Tests

```bash
./mvnw test
```

### Manual Verification

Test that same document returns same score:

```bash
# Request 1
curl -X POST http://localhost:8081/api/v1/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{"document": "TEST123", "amount": 10000, "term": 12}'

# Request 2 (should return identical score)
curl -X POST http://localhost:8081/api/v1/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{"document": "TEST123", "amount": 10000, "term": 12}'
```


