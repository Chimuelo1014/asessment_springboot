# API Gateway - CoopCredit

## Overview

The API Gateway serves as the single entry point for all CoopCredit microservices, providing centralized routing, load balancing, and cross-cutting concerns like CORS and request filtering.

## Technology Stack

- **Spring Cloud Gateway**: Reactive gateway built on Spring WebFlux
- **Spring Boot Actuator**: Health checks and metrics
- **Micrometer**: Metrics collection with Prometheus support

## Architecture

```
Client Request → API Gateway (8090) → Routes to:
                                      ├─ Credit Application Service (8080)
                                      └─ Risk Central Service (8081)
```

## Features

### 1. Intelligent Routing
- **Credit Application Service**: `/api/v1/auth/**`, `/api/v1/affiliates/**`, `/api/v1/credit-applications/**`
- **Risk Central Service**: `/api/v1/risk-evaluation/**`
- **Actuator Endpoints**: `/services/{service-name}/actuator/**`

### 2. Cross-Cutting Concerns
- **CORS Configuration**: Allows requests from any origin (configurable)
- **Request Headers**: Adds `X-Gateway-Route` header to identify routing
- **Response Time**: Tracks and adds response time header
- **Correlation ID**: Propagates distributed tracing IDs

### 3. Observability
- **Health Checks**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **Gateway Routes**: `/actuator/gateway/routes`

### 4. Resilience (Ready for Enhancement)
- Circuit breaker configuration prepared
- Timeout configuration: 5s connect, 30s response
- Retry logic (can be added)

## Quick Start

### Local Development

```bash
cd api-gateway
./mvnw spring-boot:run
```

Gateway will start on port **8090**.

### Docker

```bash
# Build image
docker build -t coopcredit-gateway .

# Run container
docker run -p 8090:8090 \
  -e CREDIT_APP_URL=http://localhost:8080 \
  -e RISK_SERVICE_URL=http://localhost:8081 \
  coopcredit-gateway
```

### Docker Compose

```bash
# From project root
docker-compose up api-gateway
```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Gateway port | 8090 |
| `CREDIT_APP_URL` | Credit application service URL | http://localhost:8080 |
| `RISK_SERVICE_URL` | Risk central service URL | http://localhost:8081 |

### Route Configuration

Routes are configured in `application.properties`:

```properties
# Example route configuration
spring.cloud.gateway.routes[0].id=credit-application-service
spring.cloud.gateway.routes[0].uri=http://localhost:8080
spring.cloud.gateway.routes[0].predicates[0]=Path=/pay/v1/auth/**
```

## API Examples

### Through Gateway (Recommended)

```bash
# Register user (via gateway)
curl -X POST http://localhost:8090/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"pass","email":"test@example.com"}'

# Create affiliate (via gateway)
curl -X POST http://localhost:8090/api/v1/affiliates \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"document":"123","fullName":"Test","email":"test@test.com","monthlySalary":3000000}'

# Evaluate risk (via gateway)
curl -X POST http://localhost:8090/api/v1/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{"document":"123","amount":5000000,"term":24}'
```

### Gateway Status

```bash
# Health check
curl http://localhost:8090/actuator/health

# View all routes
curl http://localhost:8090/actuator/gateway/routes

# Metrics
curl http://localhost:8090/actuator/metrics
```

## Response Headers

The gateway adds the following headers to responses:

- `X-Correlation-Id`: Unique request identifier for tracing
- `X-Gateway-Route`: Which backend service handled the request
- `X-Response-Time`: Processing time (if configured)
- `Access-Control-Allow-*`: CORS headers

## Monitoring

### Metrics Available

- `gateway.requests`: Total requests through gateway
- `http.server.requests`: Request metrics by route
- `spring.cloud.gateway.requests`: Gateway-specific metrics

### Prometheus Integration

```bash
# Scrape metrics
curl http://localhost:8090/actuator/prometheus
```

Example Prometheus configuration:

```yaml
scrape_configs:
  - job_name: 'api-gateway'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8090']
```

## Benefits of Using the Gateway

### 1. **Single Entry Point**
- Simplified client configuration
- Centralized security policies
- Consistent API versioning

### 2. **Load Balancing**
- Can route to multiple instances
- Automatic failover (with circuit breaker)
- Health-based routing

### 3. **Security**
- Rate limiting (can be added)
- IP filtering (can be added)
- JWT validation at gateway level (optional)

### 4. **Monitoring**
- Centralized logging
- Request tracing across services
- Performance metrics aggregation

### 5. **Flexibility**
- Route requests based on headers, params, etc.
- Transform requests/responses
- Add/remove headers dynamically

## Advanced Configuration

### Enable Circuit Breaker

Add to `application.properties`:

```properties
spring.cloud.gateway.default-filters[2]=name=CircuitBreaker
spring.cloud.gateway.default-filters[2].args.name=defaultCircuitBreaker
spring.cloud.gateway.default-filters[2].args.fallbackUri=forward:/fallback
```

### Add Rate Limiting

```properties
spring.cloud.gateway.routes[0].filters[1]=name=RequestRateLimiter
spring.cloud.gateway.routes[0].filters[1].args.redis-rate-limiter.replenishRate=10
spring.cloud.gateway.routes[0].filters[1].args.redis-rate-limiter.burstCapacity=20
```

### Custom Filters

Create a `GatewayFilterFactory` bean to add custom logic:

```java
@Component
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Request: {}", exchange.getRequest().getPath());
            return chain.filter(exchange);
        };
    }
}
```

## Troubleshooting

### Gateway not routing

1. Check service URLs are correct
2. Verify backend services are running
3. Check health endpoints: `/actuator/health`

### CORS issues

Update CORS configuration in `application.properties`:

```properties
spring.cloud.gateway.globalcors.cors-configurations.[/**].allowed-origins=http://localhost:3000
```

### Timeout errors

Increase timeout values:

```properties
spring.cloud.gateway.httpclient.connect-timeout=10000
spring.cloud.gateway.httpclient.response-timeout=60s
```

## Architecture Diagram

```
┌──────────────┐
│   Client     │
└──────┬───────┘
       │
       ▼
┌─────────────────────────────────┐
│      API Gateway (8090)         │
│  ┌──────────────────────────┐  │
│  │  Spring Cloud Gateway    │  │
│  │  - Routing               │  │
│  │  - Load Balancing        │  │
│  │  - CORS                  │  │
│  │  - Metrics               │  │
│  └──────────────────────────┘  │
└─────────┬───────────────────────┘
          │
     ┌────┴─────┐
     │          │
     ▼          ▼
┌─────────┐ ┌──────────┐
│ Credit  │ │   Risk   │
│ Service │ │ Service  │
│  :8080  │ │  :8081   │
└─────────┘ └──────────┘
```

## Performance Considerations

- Gateway adds ~2-5ms latency per request
- Uses reactive WebFlux (non-blocking)
- Can handle 1000+ concurrent requests
- Memory footprint: ~200-300MB

## Security Notes

- Gateway does NOT validate JWTs (delegated to services)
- For production, add:
  - Rate limiting
  - IP whitelisting
  - API key validation
  - Request size limits

## Integration with Services

Services should be configured to:
1. Accept requests from gateway IP
2. Trust `X-Forwarded-*` headers
3. Use correlation IDs from gateway

## Future Enhancements

- [ ] Service discovery (Eureka/Consul)
- [ ] API versioning strategies
- [ ] Request/Response caching
- [ ] GraphQL gateway support
- [ ] WebSocket routing

## Contributing

When adding new routes:
1. Update `application.properties`
2. Add route documentation here
3. Update Postman collection
4. Test via gateway

## License

© 2024 CoopCredit - Academic Project
