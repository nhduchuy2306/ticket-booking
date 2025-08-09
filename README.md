**Microservice Architecture for a Ticketing System**

**Description**
This is a GYP(TicketBox) this support to Organizer to register to buy ticket, user can choose seat, buy ticket, generate
to PDF, QR code and use this at the event.

### 1. Core Services

#### 1.1. Event Service

- Manage event information (create, update, view details)
- List events by criteria (location, time, category)
- Setup SeatMap
- Approve events (if required)

#### 1.2. Ticket Service

- Manage ticket information (ticket type, price, quantity)
- Check ticket availability
- Create Type of Ticket
- Manage inventory (remaining ticket quantity)
- Export Pdf, QR Code

#### 1.3. Order Service

- Handle ticket ordering process
- Manage shopping cart
- Handle payments (may integrate with Payment Service)
- Track order history
- Revenue reports by event
- Ticket sales trend analysis
- Attendee analytics
- Handle different payment methods
- Integrate with third-party payment gateways
- Manage invoices

#### 1.4. SaleChannel Service
- Create and manage sale channels (online, offline) -> Create Box Office channel, Create Online channel
- Manage sale channels (online, offline)
- Track sales by channel
- Integrate with third-party sales platforms (if needed)
---

### 2. Supporting Services

#### 2.1. Auth Service

- Manage user information (both organizers and customers)
- Handle authorization (inherited from existing auth system)
- Profile management

#### 2.2. Notification Service

- Send confirmation emails/ticket booking success
- Notify about upcoming events
- Send reminders before events

#### 2.3. Review Service (optional)

- Post-event feedback
- System rating

---

### 3. Infrastructure Services

#### 3.1. API Gateway

- Single entry point for all clients
- Handle authentication/authorization
- Route requests to respective services

#### 3.2. Service Discovery

- Manage service addresses
- Load balancing

#### 3.3. Config Service

- Centralized configuration management

---

### 4. Main Data Flow

1. Organizer creates event, setup seat map → Event Service
2. Organizer adds ticket types → Ticket Service → Generate total of Ticket, time to sale ticket
3. Customer views event list → Event Service
4. Customer selects ticket → Ticket Service (check availability) check the remain unavailable ticket
5. Customer creates order → Order Service
6. Customer makes payment → Payment Service
7. System sends confirmation → Notification Service → Send QR code and PDF ticket to user when payment successfully

---

### 5. Setup port for each service
# Internal Service
- AuthService -> port:9000
- EventService -> port:9001
- TicketService -> port:9002
- OrderService -> 9003
- SaleChannelService -> port:9004
- NotificationService -> 9005
- Observability -> 9007
- Configuration -> 9008
- DiscoveryService -> port:9761
- ApiGateway -> 9999
# Other Service
- Zipkin -> 9411
- Kafka -> 9092
- Kafka-ui -> 9090
- Mail-dev -> 1080
- redis -> 6379
- mysql -> 13306
---

### 6. Swagger
- Api Gateway: http://localhost:{port}/webjars/swagger-ui/index.html
- Other Service: http://localhost:{port}/swagger-ui/index.html

---

## 7. Note
- Use profile "no-kafka" to not use kafka
- Use profile "no-cache" to not use cache