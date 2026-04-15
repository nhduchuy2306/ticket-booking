CREATE TABLE IF NOT EXISTS customerrefreshtoken (
    id           VARCHAR(255) PRIMARY KEY,
    customer_id  VARCHAR(255) NOT NULL,
    token        VARCHAR(500) NOT NULL UNIQUE,
    expiry       DATETIME     NOT NULL,
    revoked      BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE
);