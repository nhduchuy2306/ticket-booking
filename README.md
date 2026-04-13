# GYP Ticket Booking Microservices

GYP/TicketBox ticket booking microservices for organizers and customers: event creation, seat configuration, ticket sales, payment, PDF/QR generation, and ticket validation at the entrance.

This README is the root-level guide for the whole project: it describes the architecture, modules, ports, startup flow, profiles, and the main things to know before cloning or running the system locally.

---

## 1. Architecture Overview

This project is a **multi-module Maven** system built with **Spring Boot 3.4.5**, **Java 21**, and a microservices architecture.

### High-level flow

1. Users sign in or register through `auth-service`.
2. Requests go through `api-gateway` and are routed to the appropriate service.
3. `discovery-service` (Eureka) manages service registration and load balancing.
4. Services communicate through:
   - internal HTTP / Feign / WebClient calls
   - Kafka for asynchronous workflows
   - Redis for seat hold, cache, and temporary inventory data
   - MySQL for core business data
5. `notification-service` handles notifications, emails, and attachments.
6. `web-ui` is the standalone Vite/React frontend.

### Maven reactor modules

- `common-service`
- `api-gateway`
- `auth-service`
- `discovery-service`
- `notification-service`
- `event-service`
- `ticket-service`
- `order-service`
- `salechannel-service`

---

## 2. Service Responsibilities

### 2.1 `auth-service`
- User registration and login
- JWT issuance
- User, group, and permission management inside token claims
- Authentication foundation for the whole system

### 2.2 `event-service`
- Event CRUD
- Filtering by location, time, and category
- Seat map configuration
- Business events related to ticket generation

### 2.3 `ticket-service`
- Ticket type, price, and quantity management
- Inventory and availability checks
- Ticket inventory creation by event and seat map
- PDF and QR code generation for tickets
- Ticket validation when scanning at the event

### 2.4 `order-service`
- Cart and checkout management
- Temporary seat holding
- Order creation and payment status handling
- Integration with an external payment gateway
- Order history, revenue tracking, and ticket sales statistics

### 2.5 `salechannel-service`
- Online and offline sales channel configuration
- Box office, online channel, and distribution partner management
- Synchronization of sales channel state with events

### 2.6 `notification-service`
- Sending booking confirmation emails
- Sending PDF / QR code files to customers
- Sending event reminders
- This module is currently **Vert.x-based**, not Spring Boot like the other services

### 2.7 `discovery-service`
- Eureka Registry
- Service registration and discovery

### 2.8 `api-gateway`
- Single entry point for clients
- Route requests by path prefix
- Shared CORS configuration
- Swagger aggregation for multiple services

### 2.9 `web-ui`
- Main frontend application
- Communicates with the API Gateway

### 2.10 `QR code scanner app`
- A separate React app for scanning and validating tickets at the event entrance
- Communicates directly with `ticket-service` for validation

---

## 3. Runtime Ports

> The table below is based on the current repository configuration.

| Component | Port | Notes |
|---|---:|---|
| `auth-service` | `9000` | Spring Boot |
| `event-service` | `9001` | Spring Boot |
| `ticket-service` | `9002` | Spring Boot |
| `order-service` | `9003` | Spring Boot |
| `salechannel-service` | `9004` | Spring Boot |
| `notification-service` | `9005` | Vert.x |
| `discovery-service` | `9761` | Eureka Server |
| `api-gateway` | `9999` | Spring Cloud Gateway |
| `web-ui` | `3000` | Vite frontend |

### Supporting infrastructure in Docker Compose

| Component | Port |
|---|---:|
| Zipkin | `9411` |
| Zookeeper | `2181` |
| Kafka | `9092` / `29092` |
| Kafka UI | `8089` |
| MailDev | `1080` / `1025` |
| Redis | `6379` |
| MySQL | `13306` |
| MinIO | `19000` / `19001` |
| Keycloak | `18080` |
| Jenkins | `9090` / `50000` |
| Prometheus | `9091` |
| Grafana | `3002` |

---

## 4. Main Data Flows

### 4.1 Event creation and ticket sales configuration
1. An organizer signs in through `auth-service`.
2. The organizer creates an event in `event-service`.
3. The organizer configures the seat map.
4. The organizer defines ticket types and inventory in `ticket-service`.
5. `salechannel-service` configures the sales channel.

### 4.2 Ticket purchase flow
1. Customers browse events from `event-service`.
2. Customers choose tickets or seats, and the system checks availability in `ticket-service`.
3. `order-service` creates the cart and order.
4. The payment gateway processes the payment.
5. After successful payment, the system emits events to:
   - update inventory
   - generate PDF / QR code
   - send confirmation emails

### 4.3 Event check-in flow
1. Staff or scanner apps scan the QR code.
2. `ticket-service` validates the ticket.
3. The system checks ticket status, event, seat, and usage history.
4. A valid ticket is marked as used.

---

## 5. Internal Integration and Communication

### 5.1 Eureka
Spring Boot services register with `discovery-service` and call each other by service name.

### 5.2 Gateway routes
The gateway currently routes by prefix:

- `/auths/**` → `auth-service`
- `/events/**` → `event-service`
- `/tickets/**` → `ticket-service`
- `/salechannels/**` → `salechannel-service`
- `/orders/**` → `order-service`
- `/notifications/**` → `notification-service`
- `/bff/**` → `bff-service`

### 5.3 Kafka
The repository contains Kafka flows for business processes such as:
- create / update / delete event
- generate ticket PDFs
- send notification emails
- synchronize data between services

### 5.4 Redis
Redis is used for:
- temporary seat holding
- inventory caching
- query optimization in the ticket purchasing flow

---

## 6. Security and Authorization

The system uses JWT with permission claims embedded in the token.

### Example claim structure

```json
{
  "sub": "user-id",
  "userId": "user-id",
  "organizationId": "org-id",
  "permissions": {
    "EVENT": ["READ", "CREATE", "UPDATE"],
    "TICKET": ["READ"],
    "ADMIN": []
  }
}
```

### Permission checking

Each service decodes JWT using a `JwtDecoder` bean and validates permissions with a custom evaluator. For example:

`@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")`

The gateway does not perform stateful token validation; the token is passed down to downstream services.

---

## 7. Environment Configuration

### 7.1 Maven profiles
The root `pom.xml` currently defines two profiles:
- `dev`
- `prod`

### 7.2 Application profiles
Some services include profile-specific configuration files such as:
- `application.properties`
- `application-dev.properties`
- `application-prod.properties`

### 7.3 Feature-toggle profiles
The project documentation mentions:
- `no-kafka`
- `no-cache`

If a service supports these profiles, they can be used to disable Kafka or Redis when a lighter runtime is needed.

### 7.4 Environment variables

The repository references the following variables in `.env` or the runtime environment:
- `JWT_SECRET_TOKEN`
- `MOMO_PARTNER_CODE`
- `MOMO_ACCESS_KEY`
- `MOMO_SECRET_KEY`

---

## 8. Running Locally

### 8.1 Requirements
- Java 21
- Maven 3.9+ or the Maven Wrapper `mvnw`
- Docker and Docker Compose if you want to run the full stack

### 8.2 Build the whole project

```powershell
mvn clean install -DskipTests -P dev
```

### 8.3 Recommended startup order

1. `discovery-service`
2. `api-gateway`
3. Business services: `auth-service`, `event-service`, `ticket-service`, `order-service`, `salechannel-service`, `notification-service`
4. `web-ui`

### 8.4 Run a Spring Boot service

```powershell
cd C:\Users\NNHY\My_Data\coding\ticket-booking\auth-service
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Repeat the same pattern for the other Spring Boot services and change the profile if needed.

### 8.5 Run `notification-service`

`notification-service` is a Vert.x app, so it uses a different startup command from the Spring Boot services:

```powershell
cd C:\Users\NNHY\My_Data\coding\ticket-booking\notification-service
.\mvnw.cmd clean compile exec:java
```

### 8.6 Run the `web-ui` frontend

The main frontend lives in `frontend/gyp-core-ui`:

```powershell
cd C:\Users\NNHY\My_Data\coding\ticket-booking\frontend\gyp-core-ui
npm install
npm run dev
```

### 8.7 Run with Docker Compose

```powershell
cd C:\Users\NNHY\My_Data\coding\ticket-booking\infrastructure\dockers
docker compose up --build
```

#### Notes for Docker Compose

- The compose setup currently references `bff-service` at `../../bff-service`.
- In the current workspace, the corresponding `bff-service` folder/module is not present.
- `notification-service` exists in the Maven reactor but is not declared as a separate service in the compose setup.

If you want to run the full stack without errors, check these two points first.

---

## 9. Swagger / API Documentation

### Gateway Swagger

```text
http://localhost:9999/webjars/swagger-ui/index.html
```

### Swagger for each service

```text
http://localhost:{port}/swagger-ui/index.html
```

Examples:
- `http://localhost:9000/swagger-ui/index.html`
- `http://localhost:9001/swagger-ui/index.html`
- `http://localhost:9002/swagger-ui/index.html`

---

## 10. Main Folder Structure

```text
ticket-booking/
  api-gateway/
  auth-service/
  common-service/
  discovery-service/
  event-service/
  infrastructure/
  notification-service/
  order-service/
  salechannel-service/
  ticket-service/
  frontend/
```

### Quick notes
- `common-service`: base classes, exceptions, permissions, shared utilities
- `infrastructure/dockers`: compose files, monitoring, infrastructure containers
- `infrastructure/notes`: workflow, service descriptions, Jenkins setup
- `frontend/gyp-core-ui`: main frontend
- `frontend/scan-ui`: a separate React app for scanning and validating tickets at the event entrance

---

## 11. Repository References

- `AGENTS.md` — project architecture and conventions
- `infrastructure/notes/workflow.md` — end-to-end business flow description
- `infrastructure/notes/service-description.md` — service responsibility overview
- `infrastructure/notes/jenkins-setup.md` — CI / Jenkins notes
- `salechannel-service/README.md` — sale channel documentation
- `notification-service/README.adoc` — how to build and run the notification service
