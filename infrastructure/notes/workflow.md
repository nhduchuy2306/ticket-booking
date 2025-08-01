# TicketBox Main Flow: Organizer to Ticket Scanning

## Phase 1: Organizer Setup & Event Creation

### 1.1 Organizer Registration & Authentication
```
Flow: Organizer → API Gateway → Auth Service
```
- Organizer registers/logs in through Keycloak
- Auth Service validates credentials and creates organizer profile
- JWT token issued for subsequent requests

### 1.2 Event Creation & Setup
```
Flow: Organizer → API Gateway → Event Service
```
**Steps:**
1. **Create Event**
  - POST `/api/events`
  - Event details: name, description, date, time, location, category
  - Event status: DRAFT

2. **Setup Seat Map**
  - POST `/api/events/{eventId}/seatmap`
  - Define venue layout (sections, rows, seats)
  - Set seat categories (VIP, Premium, Regular)

3. **Event Approval** (if required)
  - PUT `/api/events/{eventId}/status` → PENDING_APPROVAL
  - Admin reviews and approves → APPROVED

### 1.3 Ticket Configuration
```
Flow: Organizer → API Gateway → Ticket Service
```
**Steps:**
1. **Create Ticket Types**
  - POST `/api/tickets/types`
  - Define ticket categories with pricing
  - Set quantity limits per type
  - Configure sale periods (start/end dates)

2. **Generate Ticket Inventory**
  - POST `/api/tickets/inventory/generate`
  - Create individual tickets based on seat map + ticket types
  - Initialize inventory counts

### 1.4 Sales Channel Configuration
```
Flow: Organizer → API Gateway → SaleChannel Service
```
- Configure sales channels (online, mobile app, physical outlets)
- Set commission rates and payment methods per channel

---

## Phase 2: Customer Ticket Purchase Journey

### 2.1 Event Discovery
```
Flow: Customer → API Gateway → Event Service
```
- GET `/api/events` - Browse available events
- GET `/api/events/{eventId}` - View event details
- GET `/api/events/{eventId}/seatmap` - View available seats

### 2.2 Ticket Selection & Cart Management
```
Flow: Customer → API Gateway → Ticket Service + Order Service
```
**Steps:**
1. **Check Ticket Availability**
  - GET `/api/tickets/availability/{eventId}`
  - Real-time inventory check via Redis cache

2. **Select Seats/Tickets**
  - POST `/api/tickets/reserve` - Temporary seat hold (5-10 minutes)
  - PUT `/api/orders/cart/add` - Add to shopping cart

3. **Cart Management**
  - GET `/api/orders/cart` - View cart contents
  - PUT `/api/orders/cart/update` - Modify quantities
  - DELETE `/api/orders/cart/remove` - Remove items

### 2.3 Checkout & Payment
```
Flow: Customer → API Gateway → Order Service → Payment Gateway
```
**Steps:**
1. **Create Order**
  - POST `/api/orders/create`
  - Validate inventory availability
  - Calculate total price (including fees)
  - Order status: PENDING_PAYMENT

2. **Process Payment**
  - POST `/api/orders/{orderId}/payment`
  - Integrate with payment gateway (Stripe, PayPal, etc.)
  - Handle different payment methods (card, wallet, bank transfer)

3. **Payment Confirmation**
  - Payment success → Order status: CONFIRMED
  - Payment failure → Release reserved seats → Order status: FAILED

### 2.4 Ticket Generation & Delivery
```
Flow: Order Service → Ticket Service → Notification Service
```
**Steps:**
1. **Generate Digital Tickets**
  - POST `/api/tickets/generate/{orderId}`
  - Create PDF tickets with QR codes
  - Update inventory (decrement available quantity)

2. **Send Confirmation**
  - Kafka event: `ORDER_CONFIRMED`
  - Notification Service sends email with:
    - Order confirmation
    - PDF tickets attached
    - QR codes for each ticket

---

## Phase 3: Event Day & Ticket Validation

### 3.1 Ticket Scanning Setup
```
Flow: Event Staff → API Gateway → Ticket Service
```
- Staff login with scanner device/app
- GET `/api/events/{eventId}/scan-config` - Load event scanning configuration

### 3.2 Ticket Validation Process
```
Flow: Scanner App → API Gateway → Ticket Service
```
**Steps:**
1. **Scan QR Code**
  - POST `/api/tickets/validate`
  - Decode QR code to extract ticket ID and validation hash

2. **Validate Ticket**
  - Check ticket authenticity
  - Verify ticket hasn't been used (prevent double-entry)
  - Confirm ticket is for correct event/date
  - Validate seat assignment (if applicable)

3. **Entry Response**
  - Valid ticket → Mark as USED → Allow entry
  - Invalid/Used ticket → Deny entry → Log attempt
  - Show attendee info on scanner device

### 3.3 Real-time Analytics
```
Flow: Ticket Service → Analytics Dashboard
```
- Real-time entry tracking
- Attendance monitoring
- No-show analysis
- Revenue reporting

---

## Phase 4: Post-Event Activities

### 4.1 Event Completion
```
Flow: System → Event Service → Analytics
```
- Automatic event status update to COMPLETED
- Generate final attendance reports
- Calculate final revenue and commission

### 4.2 Review & Feedback (Optional)
```
Flow: Customer → API Gateway → Review Service
```
- Send post-event survey via Notification Service
- Collect feedback and ratings
- Analytics for organizer improvement

---

## Key Integration Points & Data Flow

### Real-time Inventory Management
```
Ticket Service ↔ Redis Cache ↔ Order Service
```
- Seat reservations with TTL
- Inventory updates via Kafka events
- Conflict resolution for concurrent purchases

### Event-Driven Communication (Kafka Topics)
- `EVENT_CREATED` → Notification Service (welcome email)
- `TICKET_RESERVED` → Inventory update
- `ORDER_CONFIRMED` → Ticket generation + Email notification
- `TICKET_SCANNED` → Real-time analytics update

### Authentication & Authorization Flow
```
All Requests → API Gateway → Auth Service (JWT validation) → Target Service
```
- Role-based access (Organizer, Customer, Staff, Admin)
- Service-to-service authentication via service tokens

### Error Handling & Resilience
- Circuit breaker patterns for external payment gateways
- Retry mechanisms for failed operations
- Compensation transactions for order failures
- Dead letter queues for failed messages

---

## Development Priority Order

1. **Core Foundation** (Weeks 1-2)
  - Auth Service + API Gateway
  - Basic Event Service (CRUD operations)
  - Database setup + service discovery

2. **Event Management** (Weeks 3-4)
  - Complete Event Service with seat mapping
  - Ticket Service with inventory management
  - Basic Order Service

3. **Purchase Flow** (Weeks 5-6)
  - Shopping cart functionality
  - Payment integration
  - Notification Service for confirmations

4. **Ticket Generation** (Weeks 7-8)
  - PDF generation with QR codes
  - Ticket validation API
  - Scanner application/interface

5. **Advanced Features** (Weeks 9-10)
  - Real-time analytics
  - Advanced reporting
  - Review system
  - Performance optimization

This flow ensures a complete ticket lifecycle from creation to validation while maintaining the microservice architecture principles and leveraging your specified technology stack.