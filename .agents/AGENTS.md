# AGENTS.md - GYP Ticket Booking Microservices

## Architecture Overview
This is a Spring Boot 3.4.5 multi-module microservices system (Java 21) for ticket booking with services coordinated via Eureka Service Discovery (port 9761). API Gateway (9999) routes all requests; each service has a dedicated port (9000-9010).

### Service Map
| Service | Port | Description |
|---------|------|-------------|
| discovery-service | 9761 | Eureka Service Registry |
| config-service | 9006 | Spring Cloud Config Server (native backend) |
| api-gateway | 9999 | Spring Cloud Gateway |
| auth-service | 9000 | JWT/Keycloak auth, permissions |
| event-service | 9001 | Event CRUD, seat mapping, Kafka events |
| ticket-service | 9002 | Ticket inventory, PDF/QR generation |
| order-service | 9003 | Cart, checkout, MoMo payment |
| salechannel-service | 9004 | Online/offline channel config |
| notification-service | 9005 | Email notifications (Spring Boot) |

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

### Centralized Config (Spring Cloud Config Server)
- **Config Server**: `config-service` serves all config via native filesystem backend
- **Config Location**: `config-service/src/main/resources/configurations/`
- **Structure**: `{service}.yml` (shared), `dev/{service}-dev.yml`, `prod/{service}-prod.yml`
- **Shared configs**: `application.yml` (all services), `database.yml` (DB services via `spring.cloud.config.name`)
- **Startup order**: `discovery-service` → `config-service` → all other services
- Each service bootstrap: `spring.config.import=optional:configserver:http://localhost:9006`

## Database
- MySQL per-service schemas (dev: localhost:13306)
- Flyway migrations in `src/main/resources/db/migration/`
- Hibernate ddl-auto=update for dev

## Infrastructure
- Docker Compose in `infrastructure/dockers/docker-compose.yml`
- Keycloak (18080), MinIO (19000), Kafka (9092)
- **Redis Sentinel**: master (6379) + slave (6380) + 3 sentinels (26379-26381), master name: `mymaster`
- Profiles: `dev` (default), `prod` (Docker), `no-kafka`, `no-cache`
