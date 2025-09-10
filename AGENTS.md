# AGENTS.md - GYP Ticket Booking Microservices

## Architecture Overview
This is a Spring Boot 3.4.5 multi-module microservices system (Java 21) for ticket booking with 12 services coordinated via Eureka Service Discovery (port 9761). API Gateway (9999) routes all requests; each service has a dedicated port (9000-9010).

### Core Service Boundaries
- **Auth Service (9000)**: JWT/Keycloak-based auth; permission matrix in UserGroup→PermissionItems
- **Event Service (9001)**: Event CRUD + Seat Mapping; publishes Kafka events for ticket generation
- **Ticket Service (9002)**: Inventory, PDF/QR generation via Kafka listeners; Redis caching
- **Order Service (9003)**: Cart + Checkout; Feign calls to MoMo payment gateway; Redis for seat holds
- **Sale Channel Service (9004)**: Online/offline channel config; Kafka syncs with events
- **Auth/Config/Discovery**: Infrastructure services
- **BFF Service (9010)**: WebClient.Builder (@LoadBalanced) aggregates Event + SaleChannel data
- **Notification, Observability**: Support services

## Critical Data Flow & Integration Points

### Authentication Chain
1. **Login/Register** → Auth Service returns JWT with embedded permissions: `{sub, userId, organizationId, permissions: {APP_ID: [READ, CREATE, UPDATE]}}`
2. **API Gateway** passes Authorization header to services (stateless, no token validation there)
3. **Each Service** decodes JWT via `JwtDecoder` bean (HS512 symmetric) + validates via `CustomPermissionEvaluator`
4. **Permission Check**: `@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")` → evaluates against JWT permissions claim

### Kafka Event Topics (in common-service/TopicConstants)
- `event.command.create/update/delete` → Event Service producers
- `generate.ticket.pdf.event` → Ticket Service listener generates PDFs
- `send-email.event` → Ticket Service listener sends confirmations
- `order.created.event` → Event Service listener tracks orders
- `event.on.sale.event` → Sale Channel sync

### Service-to-Service Communication
- **Order Service** → Feign client to MoMo (external payment): `@FeignClient(name="momo-service", url="${momo.endpoint}")`
- **BFF Service** → WebClient with @LoadBalanced DNS: `http://EVENT-SERVICE/events/{id}` (Eureka resolves)
- **Internal** services use @EnableFeignClients for discovery-based calls

## Project-Specific Conventions

### Response Format
All endpoints return `ApiResponse<T>` (code, message, result) via `GlobalExceptionHandler` or service methods.

### Code Structure Per Service
```
{service}/
  src/main/java/com/gyp/{service}/
    controllers/    # extend AbstractController (auth/response helpers)
    services/       # business logic; @Service, @RequiredArgsConstructor
    repositories/   # JpaRepository + @Specification for filtering
    entities/       # @Entity, extend AbstractEntity
    dtos/           # MapStruct mappers (AbstractMapper)
    configurations/ # SecurityConfiguration, SwaggerConfiguration, AppConfiguration
    messages/       # producers/ (Kafka publishers), consumers/ (@KafkaComponent, @KafkaListener)
  src/main/resources/
    application.properties (dev defaults: localhost:13306 MySQL, localhost:9092 Kafka, localhost:6379 Redis)
    application-{profile}.properties (prod: Docker hostnames)
  pom.xml           # inherits spring-boot-starter-parent 3.4.5
```

### Database & Migration
- Flyway enabled on all services; each has own schema (e.g., `auth_service`, `event_service`)
- Hibernate ddl-auto=update for dev; migrations in `src/main/resources/db/migration/`

### Profiles & Optional Features
- **Profiles**: `dev` (default), `prod` (Docker compose uses prod)
- **Disabled by Profile**: "no-kafka" (skips Kafka), "no-cache" (skips Redis)
- **Environment Variables**: `JWT_SECRET_TOKEN`, `MOMO_PARTNER_CODE`, `MOMO_ACCESS_KEY`, `MOMO_SECRET_KEY` from `.env`

## Build & Deployment Workflows

### Local Development
```bash
mvn clean install -DskipTests -P dev
cd {service} && mvn spring-boot:run -Dspring-boot.run.profiles=dev
# Or: discovery-service first on 9761, then api-gateway, then other services
# Docker: cd infrastructure/dockers && docker-compose up
```

### Docker (Production)
All services have Dockerfile; compose in `infrastructure/dockers/docker-compose.yml` starts 13 containers (Zookeeper, Kafka, MySQL, Redis, Keycloak, MinIO, all services). Database migrations run on startup.

### Testing
- `@SpringBootTest` in all modules; add `-P dev` to mvn test
- No integration tests checked in; focus on unit tests (services, controllers)

## Permission & Authorization Patterns

### Permission Matrix (JWT Claim Structure)
```json
{
  "sub": "user-id",
  "organizationId": "org-id",
  "permissions": {
    "EVENT": ["READ", "CREATE", "UPDATE"],
    "TICKET": ["READ"],
    "ADMIN": []  // if admin=true
  }
}
```

### Decorator Usage
`@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")` — validates action on resource before method enters. Enums `ApplicationPermission` (EVENT, TICKET, etc.) and `ActionPermission` (READ, CREATE, UPDATE, DELETE) defined in common-service.

## External Dependencies & Third-Party Integration

### Payment Gateway
- **MoMo** (Vietnam): Order Service calls via Feign; POST to `https://test-payment.momo.vn/v2/gateway/api/create` with partner code/access/secret keys
- Mocking via MailDev (port 1080 SMTP for email testing)

### Infrastructure
- **Keycloak** (18080): IAM backbone; docker-compose bootstraps admin:admin
- **MinIO** (19000/19001): S3-like object storage for event logos, PDFs
- **Zipkin** (9411): Distributed tracing (not explicitly wired in code yet)
- **Redis** (6379): Seat hold TTL, inventory cache; used by Order + Ticket services

## Common Pitfalls & Quirks

1. **JWT Symmetric Secret**: HS512 with hardcoded secret string (no public/private key); all services decode with same key → shared secret must be secure & rotated
2. **Permission Claim Format**: Deeply nested JSON (`permissions: {appId: [actions]}`) — ensure custom claim serialization during token generation matches deserialization
3. **Null Checks**: Auth/Order services return `null` on failure (login, refresh) instead of throwing exceptions → check for null in callers
4. **CORS at Gateway & App Level**: Both api-gateway and common SecurityConfiguration define CORS; can cause conflicts
5. **Kafka Consumers Without Error Handlers**: Failed message processing logs error but doesn't retry or DLQ → failed PDF generation silently lost
6. **No Token Blacklist/Revocation**: Tokens valid until expiry (1 day); logout not implemented
7. **Admin Flag**: Checks if first UserGroup is admin → assumes single primary group per user

## Key File References
- `pom.xml`: Module list, Spring Cloud versions
- `README.md`, `infrastructure/notes/workflow.md`: Data flow & port mapping
- `api-gateway/src/main/resources/application.yml`: Route definitions
- `common-service/src/main/java/com/gyp/common/`: Base classes, exceptions, configs
- `auth-service/src/main/java/com/gyp/authservice/services/JwtTokenProvider.java`: Token generation logic
- `infrastructure/dockers/docker-compose.yml`: Environment config (MySQL:13306 dev, mysql:3306 prod)

## Debugging & Monitoring Tips
- Enable Spring Gateway DEBUG logging in `application-dev.yml` (logging.level.org.springframework.cloud.gateway=DEBUG)
- Kafka topics visible in kafka-ui (localhost:8089)
- Each service Swagger: `http://localhost:{port}/swagger-ui.html` (aggregated at gateway /webjars/swagger-ui/)
- Logs via stdout (container) or file (local); Zipkin traces at 9411 if enabled

