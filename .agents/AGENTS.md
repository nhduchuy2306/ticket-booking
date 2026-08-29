# AGENTS.md - GYP Ticket Booking Microservices

## Architecture Overview
This is a Spring Boot 3.4.5 multi-module microservices system (Java 21) for ticket booking with services coordinated via Eureka Service Discovery (port 9761). API Gateway (9999) routes all requests; each service has a dedicated port (9000-9010).

### Service Map
| Service | Port | Description |
|---------|------|-------------|
| discovery-service | 9761 | Eureka Service Registry |
| api-gateway | 9999 | Spring Cloud Gateway |
| auth-service | 9000 | JWT/Keycloak auth, permissions |
| event-service | 9001 | Event CRUD, seat mapping, Kafka events |
| ticket-service | 9002 | Ticket inventory, PDF/QR generation |
| order-service | 9003 | Cart, checkout, MoMo payment |
| salechannel-service | 9004 | Online/offline channel config |
| notification-service | 9005 | Email notifications (Spring Boot) |
| bff-service | 9010 | BFF aggregator (WebClient @LoadBalanced) |

## Build System
- **Gradle** multi-module project
- Root: `build.gradle` + `settings.gradle` + `gradle.properties`
- Each service: `{service-name}/{service-name}.gradle`
- All versions centralized in `gradle.properties`
- Build: `.\gradlew.bat clean build -x test`

## Code Structure Per Service
```
{service}/src/main/java/com/gyp/{servicename}/
  controllers/    # extends AbstractController
  services/       # @Service, @RequiredArgsConstructor
  repositories/   # JpaRepository + @Specification
  entities/       # @Entity, extends AbstractEntity
  dtos/           # MapStruct mappers (extends AbstractMapper)
  configurations/ # Security, Swagger, App configs
  mappers/        # MapStruct interfaces
  messages/       # Kafka producers/ and consumers/
```

## Critical Patterns

### Lombok + @Builder
- Entities use `@Builder` (NOT `@SuperBuilder`)
- **Any field with initializer** (e.g., `= new ArrayList<>()`) **MUST** have `@Builder.Default`

### MapStruct
- All mappers: `unmappedTargetPolicy = ReportingPolicy.IGNORE`
- **NEVER** use `@Mapping(target = "createUser/changeUser/...", ignore = true)` — causes "Unknown property" error because `@Builder` doesn't expose parent fields
- Use `@AfterMapping` with `mapAbstractFieldsToEntity()` for audit field population

### Authentication
- JWT HS512 symmetric key, stateless
- `@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.XXX, #ActionPerm.YYY)")`
- Permission claim: `{appId: [actions]}` in JWT

### Communication
- **Kafka**: Async events between services (topics in `common-service/TopicConstants`)
- **Eureka**: Service discovery for sync calls
- **Feign/WebClient**: REST service-to-service calls

## Database
- MySQL per-service schemas (dev: localhost:13306)
- Flyway migrations in `src/main/resources/db/migration/`
- Hibernate ddl-auto=update for dev

## Infrastructure
- Docker Compose in `infrastructure/dockers/docker-compose.yml`
- Keycloak (18080), MinIO (19000), Redis (6379), Kafka (9092)
- Profiles: `dev` (default), `prod` (Docker), `no-kafka`, `no-cache`
