-- Table ORDER
CREATE TABLE IF NOT EXISTS orders
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    event_id         VARCHAR(255),
    customer_email   VARCHAR(255),
    status           VARCHAR(50),
    total_amount     DECIMAL(15, 2),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

-- Table ORDERDETAIL
CREATE TABLE IF NOT EXISTS orderdetail
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    seat_id          VARCHAR(255),
    price            DECIMAL(15, 8),
    quantity         INT,
    order_id         VARCHAR(255) NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
);
