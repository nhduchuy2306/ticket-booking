CREATE TABLE IF NOT EXISTS seatmap
(
    id               VARCHAR(255) NOT NULL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    venue_type       VARCHAR(255) NOT NULL,
    seat_config      TEXT         NOT NULL,
    stage_config     TEXT         NOT NULL,
    event_id         VARCHAR(255) NOT NULL,
    venue_id         VARCHAR(255) NOT NULL,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (event_id) REFERENCES event (id) ON DELETE CASCADE,
    FOREIGN KEY (venue_id) REFERENCES venue (id)
);

CREATE INDEX idx_seat_config_venue_type ON seatmap (venue_type);

-- Sample 1: Rectangular venue (concert hall)
INSERT INTO seatmap (id, name, venue_type, seat_config, stage_config, event_id, venue_id, create_user, create_timestamp)
VALUES ('sm_rect_001',
        'Main Hall Seating',
        'RECTANGLE',
        '{"venueType":"OUTDOOR","seatTypeColors":{"Standard":"#00FF00"},"seatMap":{"@type":"CircleSeatMap","centerX":250,"centerY":250,"radius":200,"angleStep":15.0,"sections":[{"name":"Circle Zone","seatType":"Standard","rows":[{"rowName":"C1","seatCount":24,"price":50.0,"seats":[{"seatId":"C1-1","x":300,"y":100,"ticketTypeId":"Standard","price":50.0}]}]}]}}',
        '{"label":"Artistic Center Stage","stageX":100,"stageY":75,"stageWidth":250,"stageHeight":120,"shape":"CUSTOM","orientation":"DOWN","svgPath":"M10 10 H 90 V 90 H 10 L 10 10"}',
        'evt1',
        'ven1',
        'admin_user',
        NOW());

-- Sample 2: Round venue (theater in the round)
INSERT INTO seatmap (id, name, venue_type, seat_config, stage_config, event_id, venue_id, create_user, create_timestamp)
VALUES ('sm_round_001',
        'Circular Theater',
        'ROUND',
        '{"venueType":"INDOOR","seatTypeColors":{"VIP":"#FFD700","REGULAR":"#90EE90","ECONOMY":"#ADD8E6"},"seatMap":{"@type":"RectangleSeatMap","totalRows":3,"seatsPerRow":5,"sections":[{"name":"Main Section","seatType":"VIP","rows":[{"rowName":"A","seatCount":5,"price":150.0,"seats":[{"seatId":"A1","x":0,"y":0,"ticketTypeId":"VIP","price":150.0},{"seatId":"A2","x":1,"y":0,"ticketTypeId":"VIP","price":150.0},{"seatId":"A3","x":2,"y":0,"ticketTypeId":"VIP","price":150.0},{"seatId":"A4","x":3,"y":0,"ticketTypeId":"VIP","price":150.0},{"seatId":"A5","x":4,"y":0,"ticketTypeId":"VIP","price":150.0}]}]}]}}',
        '{"label":"Artistic Center Stage","stageX":100,"stageY":75,"stageWidth":250,"stageHeight":120,"shape":"CUSTOM","orientation":"DOWN","svgPath":"M10 10 H 90 V 90 H 10 L 10 10"}',
        'evt3',
        'ven3',
        'admin_user',
        NOW());

-- Sample 3: Custom venue (mixed layout) - FIXED JSON SYNTAX
INSERT INTO seatmap (id, name, venue_type, seat_config, stage_config, event_id, venue_id, create_user, create_timestamp)
VALUES ('sm_custom_001',
        'Festival Mixed Layout',
        'CUSTOM',
        '{"venueType":"OUTDOOR","seatTypeColors":{"PREMIUM":"#FFA07A","STANDARD":"#D3D3D3"},"seatMap":{"@type":"CustomSeatMap","svgPath":"","zones":[{"name":"Zone A","seatType":"PREMIUM","zoneX":100,"zoneY":50,"width":200,"height":150,"area":{"@type":"CircleSeatMap","centerX":100,"centerY":100,"radius":80,"angleStep":20.0,"sections":[{"name":"Premium Circle","seatType":"PREMIUM","rows":[{"rowName":"CircleRow","seatCount":18,"price":200.0,"seats":[]}]}]}}]}}',
        '{"label":"Artistic Center Stage","stageX":100,"stageY":75,"stageWidth":250,"stageHeight":120,"shape":"CUSTOM","orientation":"DOWN","svgPath":"M10 10 H 90 V 90 H 10 L 10 10"}',
        'evt2',
        'ven2',
        'admin_user',
        NOW());