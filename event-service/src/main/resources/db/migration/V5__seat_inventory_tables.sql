CREATE TABLE IF NOT EXISTS seat
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    event_id         VARCHAR(255) NOT NULL,
    seat_key         VARCHAR(255) NOT NULL,
    seat_label       VARCHAR(255),
    section_id       VARCHAR(255),
    row_id           VARCHAR(255),
    ticket_type_id   VARCHAR(255),
    status           VARCHAR(50)  NOT NULL,
    version          BIGINT,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    CONSTRAINT uk_seat_event_key UNIQUE (event_id, seat_key)
);

CREATE TABLE IF NOT EXISTS seat_hold
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    seat_id          VARCHAR(255) NOT NULL,
    event_id         VARCHAR(255) NOT NULL,
    hold_token       VARCHAR(255) NOT NULL,
    user_id          VARCHAR(255),
    expires_at       DATETIME      NOT NULL,
    status           VARCHAR(50)   NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    CONSTRAINT uk_seat_hold_event_token_seat UNIQUE (event_id, hold_token, seat_id),
    CONSTRAINT fk_seat_hold_seat FOREIGN KEY (seat_id) REFERENCES seat (id) ON DELETE CASCADE
);

CREATE INDEX idx_seat_event_status ON seat (event_id, status);
CREATE INDEX idx_seat_hold_event_token ON seat_hold (event_id, hold_token);
CREATE INDEX idx_seat_hold_expires_at ON seat_hold (expires_at);