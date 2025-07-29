-- 1. TICKETTYPE Table
CREATE TABLE tickettype
(
    id                 VARCHAR(255) PRIMARY KEY,
    event_id           VARCHAR(255) NOT NULL,
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    price              DOUBLE       NOT NULL,
    quantity_total     INT          NOT NULL,
    quantity_available INT,
    sale_start_date    DATETIME,
    sale_end_date      DATETIME,
    is_seat_based      BOOLEAN DEFAULT FALSE,
    create_user        VARCHAR(255),
    change_user        VARCHAR(255),
    create_timestamp   DATETIME,
    change_timestamp   DATETIME
);

-- 2. TICKET Table (đã gộp ticketgeneration)
CREATE TABLE ticket
(
    id               VARCHAR(255) PRIMARY KEY,
    ticket_code      VARCHAR(255) UNIQUE,             -- dùng cho QR/PDF
    ticket_type_id   VARCHAR(255),
    event_id         VARCHAR(255) NOT NULL,
    event_name       VARCHAR(255),
    event_date_time  DATETIME,
    reserved_date    DATETIME,

    seat_info        VARCHAR(255),                    -- A1, A2, VIP-1
    status           VARCHAR(50) DEFAULT 'AVAILABLE', -- AVAILABLE, RESERVED, SOLD, CANCELED
    attendee_name    VARCHAR(255),
    attendee_email   VARCHAR(255),

    qr_code_url      TEXT,
    pdf_url          TEXT,

    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,

    FOREIGN KEY (ticket_type_id) REFERENCES tickettype (id)
);

-- 3. TICKETORDERLINK Table
CREATE TABLE ticketorderlink
(
    id                VARCHAR(255) PRIMARY KEY,
    ticket_id         VARCHAR(255) NOT NULL,
    order_id          VARCHAR(255) NOT NULL,
    purchase_time     DATETIME,
    price_at_purchase DOUBLE,
    create_user       VARCHAR(255),
    change_user       VARCHAR(255),
    create_timestamp  DATETIME,
    change_timestamp  DATETIME,
    FOREIGN KEY (ticket_id) REFERENCES ticket (id)
);
