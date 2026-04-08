ALTER TABLE ticket
    ADD CONSTRAINT uk_ticket_event_seat UNIQUE (event_id, seat_id);

CREATE TABLE IF NOT EXISTS processed_kafka_event
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    consumer_name    VARCHAR(255) NOT NULL,
    event_key        VARCHAR(255) NOT NULL,
    processed_at     DATETIME     NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    CONSTRAINT uk_processed_event_consumer_key UNIQUE (consumer_name, event_key)
);

CREATE INDEX idx_processed_event_consumer ON processed_kafka_event (consumer_name, event_key);