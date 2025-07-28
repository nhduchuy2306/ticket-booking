-- Create categories table
CREATE TABLE IF NOT EXISTS category
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

-- Create venues table
CREATE TABLE IF NOT EXISTS venue
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    address          TEXT         NOT NULL,
    city             VARCHAR(255) NOT NULL,
    country          VARCHAR(255) NOT NULL,
    capacity         INT,
    latitude         DECIMAL(10, 8),
    longitude        DECIMAL(11, 8),
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

CREATE TABLE IF NOT EXISTS seatmap
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    venue_type       VARCHAR(255) NOT NULL,
    seat_config      TEXT         NOT NULL,
    stage_config     TEXT         NOT NULL,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

CREATE TABLE IF NOT EXISTS venuemap
(
    id               VARCHAR(255) NOT NULL primary key,
    name             VARCHAR(255) NOT NULL,
    height           double       NOT NULL,
    width            double       NOT NULL,
    venue_id         VARCHAR(255) NOT NULL,
    seat_map_id      VARCHAR(255) NOT NULL,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    foreign key (venue_id) references venue (id) ON DELETE CASCADE,
    foreign key (seat_map_id) references seatmap (id) ON DELETE CASCADE
);

-- Create organizers table
CREATE TABLE IF NOT EXISTS organizer
(
    id               VARCHAR(255)       NOT NULL PRIMARY KEY,
    name             VARCHAR(255)       NOT NULL,
    user_name        VARCHAR(50) UNIQUE NOT NULL,
    dob              TIMESTAMP,
    phone_number     VARCHAR(255),
    email            VARCHAR(255) UNIQUE,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

-- Create season table
CREATE TABLE IF NOT EXISTS season
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    status           VARCHAR(255) NOT NULL,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

-- Create events table
CREATE TABLE IF NOT EXISTS event
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    description      TEXT,
    status           VARCHAR(255) NOT NULL,
    start_time       TIMESTAMP    NOT NULL,
    end_time         TIMESTAMP    NOT NULL,
    door_open_time   TIMESTAMP,
    door_close_time  TIMESTAMP,
    organizer_id     VARCHAR(255) NOT NULL,
    venue_map_id     VARCHAR(255) NOT NULL,
    season_id        VARCHAR(255),
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (organizer_id) REFERENCES organizer (id),
    FOREIGN KEY (venue_map_id) REFERENCES venuemap (id),
    FOREIGN KEY (season_id) REFERENCES season (id)
);

-- Create junction table for event-categories
CREATE TABLE IF NOT EXISTS eventcategories
(
    event_id    VARCHAR(255) NOT NULL,
    category_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (event_id, category_id),
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);

-- Create ticket types table
CREATE TABLE IF NOT EXISTS tickettype
(
    id                 VARCHAR(255)   NOT NULL PRIMARY KEY,
    name               VARCHAR(255)   NOT NULL,
    description        TEXT,
    price              DECIMAL(19, 2) NOT NULL,
    color              VARCHAR(255),
    quantity_available INT            NOT NULL,
    status             VARCHAR(255)   NOT NULL,
    sale_start_date    TIMESTAMP,
    sale_end_date      TIMESTAMP,
    event_id           VARCHAR(255)   NOT NULL,
    organization_id    VARCHAR(255),
    create_user        VARCHAR(255),
    change_user        VARCHAR(255),
    create_timestamp   DATETIME,
    change_timestamp   DATETIME,
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
);

-- Create event promotions table
CREATE TABLE IF NOT EXISTS eventpromotion
(
    id               VARCHAR(255)        NOT NULL PRIMARY KEY,
    code             VARCHAR(255) UNIQUE NOT NULL,
    discount_amount  DECIMAL(19, 2)      NOT NULL,
    valid_from       TIMESTAMP           NOT NULL,
    valid_to         TIMESTAMP           NOT NULL,
    event_id         VARCHAR(255)        NOT NULL,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
);

-- Create event approvals table
CREATE TABLE IF NOT EXISTS eventapproval
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    status           VARCHAR(255) NOT NULL,
    approved_by      VARCHAR(255),
    approval_date    TIMESTAMP,
    rejection_reason TEXT,
    event_id         VARCHAR(255) NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_event_organizer ON event (organizer_id);
CREATE INDEX idx_event_venue ON event (venue_map_id);
CREATE INDEX idx_event_status ON event (status);
CREATE INDEX idx_ticket_type_event ON tickettype (event_id);
CREATE INDEX idx_ticket_type_status ON tickettype (status);
CREATE INDEX idx_event_promotion_event ON eventpromotion (event_id);
CREATE INDEX idx_event_promotion_code ON eventpromotion (code);
CREATE INDEX idx_seat_config_venue_type ON seatmap (venue_type);
