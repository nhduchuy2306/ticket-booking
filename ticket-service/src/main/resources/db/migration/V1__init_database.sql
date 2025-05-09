-- 1. TICKETTYPE Table
CREATE TABLE TICKETTYPE
(
    id                 VARCHAR(255) PRIMARY KEY,
    event_id           VARCHAR(255) NOT NULL,
    name               VARCHAR(255),
    description        TEXT,
    price              DOUBLE       NOT NULL,
    quantity_available INT,
    sale_start_date    DATETIME,
    sale_end_date      DATETIME,
    create_user        VARCHAR(255),
    change_user        VARCHAR(255),
    create_timestamp   DATETIME,
    change_timestamp   DATETIME
);

-- 2. TICKET Table
CREATE TABLE TICKET
(
    id               VARCHAR(255) PRIMARY KEY,
    event_id         VARCHAR(255) NOT NULL,
    event_name       VARCHAR(255),
    seat_info        VARCHAR(255) NOT NULL,
    ticket_type_id   VARCHAR(255) NOT NULL,
    ticket_number    VARCHAR(255) UNIQUE,
    attendee_name    VARCHAR(255),
    attendee_email   VARCHAR(255),
    event_date_time  DATETIME,
    status           VARCHAR(255),
    reserved_date    DATETIME,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (ticket_type_id) REFERENCES TICKETTYPE (id)
);

-- 3. Optional: TICKET_ORDER_LINK Table
CREATE TABLE TICKETORDERLINK
(
    id               VARCHAR(255) PRIMARY KEY,
    ticket_id        VARCHAR(255) NOT NULL,
    order_id         VARCHAR(255) NOT NULL,
    purchase_time    DATETIME,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (ticket_id) REFERENCES TICKET (id)
);