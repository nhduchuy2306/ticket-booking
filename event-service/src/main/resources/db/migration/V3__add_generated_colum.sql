ALTER TABLE event_service.event
    ADD COLUMN is_generated BOOLEAN NOT NULL DEFAULT FALSE;