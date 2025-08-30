INSERT INTO salechannel (id, change_timestamp, change_user, create_timestamp, create_user, commission_rate, description,
                         channel_name, status, channel_type, organization_id, start_sale_at, end_sale_at)
VALUES ('d0ee052f-9d8b-4311-8630-a0639ae995db', NOW(), 'admin', NOW(), 'admin', 0.05,
        'Main online ticket sales channel', 'Online Tickets',
        'ACTIVE', 'MOBILE_APP', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f', NOW(), NOW()),
       ('fc14da18-c2bb-4e73-8c0b-223adc521fbe', NOW(), 'admin', NOW(), 'admin', 0.10, 'Ticket sales at venue',
        'Box Office', 'ACTIVE',
        'BOX_OFFICE', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f', NOW(), NOW()),
       ('deb346c8-145b-4a58-abd2-f2e44b170087', NOW(), 'admin', NOW(), 'admin', 0.08, 'API partner sales',
        'Partner API Sales', 'INACTIVE',
        'API_PARTNER', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f', NOW(), NOW());

INSERT INTO salechannelevent (id, change_timestamp, change_user, create_timestamp, create_user, event_id,
                              sale_channel_id)
VALUES ('d5efefc4-039f-443b-82fe-c0af7e8a249c', NOW(), 'admin', NOW(), 'admin', '33fe6f8c-7c73-4248-99b6-5a7934af2905',
        'd0ee052f-9d8b-4311-8630-a0639ae995db'),
       ('9b75078e-9289-4172-863d-fdca7b73cbbc', NOW(), 'admin', NOW(), 'admin', '33fe6f8c-7c73-4248-99b6-5a7934af2905',
        'fc14da18-c2bb-4e73-8c0b-223adc521fbe'),
       ('575f71c1-9914-45d5-bb5a-f4454e8cea30', NOW(), 'admin', NOW(), 'admin', '33fe6f8c-7c73-4248-99b6-5a7934af2905',
        'deb346c8-145b-4a58-abd2-f2e44b170087');

INSERT INTO salechannelorder (id, change_timestamp, change_user, create_timestamp, create_user, order_id, revenue,
                              sale_channel_id)
VALUES ('sco1', NOW(), 'admin', NOW(), 'admin', 'ord1001', 250.00, 'd0ee052f-9d8b-4311-8630-a0639ae995db'),
       ('sco2', NOW(), 'admin', NOW(), 'admin', 'ord1002', 120.00, 'd0ee052f-9d8b-4311-8630-a0639ae995db'),
       ('sco3', NOW(), 'admin', NOW(), 'admin', 'ord1003', 75.00, 'fc14da18-c2bb-4e73-8c0b-223adc521fbe'),
       ('sco4', NOW(), 'admin', NOW(), 'admin', 'ord1004', 500.00, 'deb346c8-145b-4a58-abd2-f2e44b170087');
