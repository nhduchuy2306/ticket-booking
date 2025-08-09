CREATE TABLE IF NOT EXISTS eventimage
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    event_id         VARCHAR(255) NOT NULL,
    image_url        VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE
);

ALTER TABLE event
    ADD COLUMN logo_url VARCHAR(255);