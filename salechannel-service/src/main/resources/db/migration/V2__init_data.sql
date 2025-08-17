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

INSERT INTO salechannelconfig (id, change_timestamp, change_user, create_timestamp, create_user, config_data,
                               config_key, sale_channel_id, organization_id)
VALUES ('1c2a6ac8-4595-45e7-94b5-43a41ed3a2a9', NOW(), 'admin', NOW(), 'admin',
        '{"theme":"dark","logoUrl":"https://example.com/logo1.png"}',
        'UI_CONFIG', 'd0ee052f-9d8b-4311-8630-a0639ae995db', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('a1e71417-570b-4c7e-99da-0e8a3b12a697', NOW(), 'admin', NOW(), 'admin', '{"maxTicketsPerOrder":5}',
        'ORDER_LIMIT', 'd0ee052f-9d8b-4311-8630-a0639ae995db', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('778e3c85-eaeb-4a58-824b-954f3e039c14', NOW(), 'admin', NOW(), 'admin',
        '{"theme":"light","logoUrl":"https://example.com/logo2.png"}',
        'UI_CONFIG', 'fc14da18-c2bb-4e73-8c0b-223adc521fbe', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f');

INSERT INTO salechannelevent (id, change_timestamp, change_user, create_timestamp, create_user, event_id,
                              sale_channel_id)
VALUES ('d5efefc4-039f-443b-82fe-c0af7e8a249c', NOW(), 'admin', NOW(), 'admin', 'evt1',
        'd0ee052f-9d8b-4311-8630-a0639ae995db'),
       ('9b75078e-9289-4172-863d-fdca7b73cbbc', NOW(), 'admin', NOW(), 'admin', 'evt1',
        'fc14da18-c2bb-4e73-8c0b-223adc521fbe'),
       ('575f71c1-9914-45d5-bb5a-f4454e8cea30', NOW(), 'admin', NOW(), 'admin', 'evt2',
        'deb346c8-145b-4a58-abd2-f2e44b170087');

INSERT INTO salechannelorder (id, change_timestamp, change_user, create_timestamp, create_user, order_id, revenue,
                              sale_channel_id)
VALUES ('sco1', NOW(), 'admin', NOW(), 'admin', 'ord1001', 250.00, 'd0ee052f-9d8b-4311-8630-a0639ae995db'),
       ('sco2', NOW(), 'admin', NOW(), 'admin', 'ord1002', 120.00, 'd0ee052f-9d8b-4311-8630-a0639ae995db'),
       ('sco3', NOW(), 'admin', NOW(), 'admin', 'ord1003', 75.00, 'fc14da18-c2bb-4e73-8c0b-223adc521fbe'),
       ('sco4', NOW(), 'admin', NOW(), 'admin', 'ord1004', 500.00, 'deb346c8-145b-4a58-abd2-f2e44b170087');
