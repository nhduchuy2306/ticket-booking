CREATE TABLE IF NOT EXISTS category
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS season
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    status           VARCHAR(255) NOT NULL,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS venue
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    address          VARCHAR(255) NOT NULL,
    city             VARCHAR(255) NOT NULL,
    country          VARCHAR(255) NOT NULL,
    capacity         INT,
    latitude         DOUBLE,
    longitude        DOUBLE,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS seatmap
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    venue_type       VARCHAR(255) NOT NULL,
    organization_id  VARCHAR(255),
    seat_config  LONGTEXT     NOT NULL,
    stage_config LONGTEXT     NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS venuemap
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    width            DOUBLE,
    height           DOUBLE,
    organization_id  VARCHAR(255),
    venue_id         VARCHAR(255),
    seat_map_id      VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    CONSTRAINT fk_venue_map_venue FOREIGN KEY (venue_id) REFERENCES venue (id) ON DELETE CASCADE,
    CONSTRAINT fk_venue_map_seat_map FOREIGN KEY (seat_map_id) REFERENCES seatmap (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS event
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    status           VARCHAR(255) NOT NULL,
    start_time       DATETIME(6)  NOT NULL,
    end_time         DATETIME(6)  NOT NULL,
    door_open_time   DATETIME(6),
    door_close_time  DATETIME(6),
    organization_id  VARCHAR(255),
    is_generated     BOOLEAN      NOT NULL DEFAULT FALSE,
    logo_url         VARCHAR(255),
    venue_map_id     VARCHAR(255)  NOT NULL,
    season_id        VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    CONSTRAINT fk_event_venue_map FOREIGN KEY (venue_map_id) REFERENCES venuemap (id),
    CONSTRAINT fk_event_season FOREIGN KEY (season_id) REFERENCES season (id),
    KEY idx_event_venue_map_id (venue_map_id),
    KEY idx_event_status (status),
    KEY idx_event_season_id (season_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tickettype
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    color            VARCHAR(255),
    description      TEXT,
    price            DOUBLE,
    currency         VARCHAR(50),
    total_capacity   INT,
    status           VARCHAR(255) NOT NULL,
    sale_start_date  DATETIME(6),
    sale_end_date    DATETIME(6),
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS eventcategories
(
    event_id    VARCHAR(255) NOT NULL,
    category_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id, category_id),
    CONSTRAINT fk_eventcategories_event FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE,
    CONSTRAINT fk_eventcategories_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS eventtickettypes
(
    event_id       VARCHAR(255) NOT NULL,
    ticket_type_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id, ticket_type_id),
    CONSTRAINT fk_eventtickettypes_event FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE,
    CONSTRAINT fk_eventtickettypes_ticket_type FOREIGN KEY (ticket_type_id) REFERENCES tickettype (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS eventpromotion
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    code             VARCHAR(255) NOT NULL,
    discount_amount  DOUBLE       NOT NULL,
    valid_from       DATETIME(6)  NOT NULL,
    valid_to         DATETIME(6)  NOT NULL,
    organization_id  VARCHAR(255),
    event_id         VARCHAR(255)  NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    UNIQUE KEY uk_eventpromotion_code (code),
    KEY idx_eventpromotion_event_id (event_id),
    CONSTRAINT fk_eventpromotion_event FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS eventapproval
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    status           VARCHAR(255) NOT NULL,
    approved_by      VARCHAR(255),
    approval_date    DATETIME(6),
    rejection_reason TEXT,
    event_id         VARCHAR(255)  NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    KEY idx_eventapproval_event_id (event_id),
    CONSTRAINT fk_eventapproval_event FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS eventimage
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    image_url        VARCHAR(255) NOT NULL,
    event_id         VARCHAR(255)  NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    KEY idx_eventimage_event_id (event_id),
    CONSTRAINT fk_eventimage_event FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS seatinventory
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    event_id         VARCHAR(255)  NOT NULL,
    seat_key         VARCHAR(255) NOT NULL,
    seat_label       VARCHAR(255),
    section_id       VARCHAR(255),
    row_id           VARCHAR(255),
    ticket_type_id   VARCHAR(255),
    status           VARCHAR(255) NOT NULL,
    version          BIGINT,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    UNIQUE KEY uk_seat_event_key (event_id, seat_key),
    KEY idx_seatinventory_event_status (event_id, status),
    KEY idx_seatinventory_ticket_type_id (ticket_type_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS standinventory
(
    id                VARCHAR(255)  NOT NULL PRIMARY KEY,
    event_id          VARCHAR(255)  NOT NULL,
    section_id        VARCHAR(255) NOT NULL,
    ticket_type_id    VARCHAR(255)  NOT NULL,
    total_capacity    INT          NOT NULL,
    reserved_quantity INT          NOT NULL DEFAULT 0,
    sold_quantity     INT          NOT NULL DEFAULT 0,
    version           BIGINT,
    create_user       VARCHAR(255),
    change_user       VARCHAR(255),
    create_timestamp  DATETIME(6),
    change_timestamp  DATETIME(6),
    UNIQUE KEY uk_standinventory_event_section_ticket_type (event_id, section_id, ticket_type_id),
    KEY idx_standinventory_event_id (event_id),
    KEY idx_standinventory_ticket_type_id (ticket_type_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS seathold
(
    id               VARCHAR(255)  NOT NULL PRIMARY KEY,
    event_id         VARCHAR(255)  NOT NULL,
    hold_token       VARCHAR(255) NOT NULL,
    user_id          VARCHAR(255),
    expires_at       DATETIME(6)  NOT NULL,
    status           VARCHAR(255) NOT NULL,
    seat_id          VARCHAR(255)  NOT NULL,
    version          BIGINT,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME(6),
    change_timestamp DATETIME(6),
    UNIQUE KEY uk_seat_hold_event_token_seat (event_id, hold_token, seat_id),
    KEY idx_seat_hold_event_token (event_id, hold_token),
    KEY idx_seat_hold_expires_at (expires_at),
    CONSTRAINT fk_seat_hold_seat FOREIGN KEY (seat_id) REFERENCES seatinventory (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS standhold
(
    id                 VARCHAR(255)  NOT NULL PRIMARY KEY,
    event_id           VARCHAR(255)  NOT NULL,
    hold_token         VARCHAR(255) NOT NULL,
    user_id            VARCHAR(255),
    quantity           INT          NOT NULL DEFAULT 1,
    expires_at         DATETIME(6)  NOT NULL,
    status             VARCHAR(255) NOT NULL,
    stand_inventory_id VARCHAR(255)  NOT NULL,
    version            BIGINT,
    create_user        VARCHAR(255),
    change_user        VARCHAR(255),
    create_timestamp   DATETIME(6),
    change_timestamp   DATETIME(6),
    UNIQUE KEY uk_stand_hold_event_token_inventory (event_id, hold_token, stand_inventory_id),
    KEY idx_stand_hold_event_token (event_id, hold_token),
    KEY idx_stand_hold_expires_at (expires_at),
    CONSTRAINT fk_stand_hold_inventory FOREIGN KEY (stand_inventory_id) REFERENCES standinventory (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
