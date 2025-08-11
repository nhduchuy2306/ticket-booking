INSERT INTO salechannel (id, change_timestamp, change_user, create_timestamp, create_user, commission_rate, description,
                         event_id, channel_name, status, channel_type)
VALUES ('sc1', NOW(), 'admin', NOW(), 'admin', 0.05, 'Main online ticket sales channel', 'evt1', 'Online Tickets',
        'ACTIVE', 'MOBILE_APP'),
       ('sc2', NOW(), 'admin', NOW(), 'admin', 0.10, 'Ticket sales at venue', 'evt1', 'Box Office', 'ACTIVE',
        'BOX_OFFICE'),
       ('sc3', NOW(), 'admin', NOW(), 'admin', 0.08, 'API partner sales', 'evt2', 'Partner API Sales', 'INACTIVE',
        'API_PARTNER');

INSERT INTO salechannelconfig (id, change_timestamp, change_user, create_timestamp, create_user, config_data,
                               config_key, sale_channel_id)
VALUES ('cfg1', NOW(), 'admin', NOW(), 'admin', '{"theme":"dark","logoUrl":"https://example.com/logo1.png"}',
        'UI_CONFIG', 'sc1'),
       ('cfg2', NOW(), 'admin', NOW(), 'admin', '{"maxTicketsPerOrder":5}', 'ORDER_LIMIT', 'sc1'),
       ('cfg3', NOW(), 'admin', NOW(), 'admin', '{"theme":"light","logoUrl":"https://example.com/logo2.png"}',
        'UI_CONFIG', 'sc2');

INSERT INTO salechannelevent (id, change_timestamp, change_user, create_timestamp, create_user, end_sale_at, event_id,
                              start_sale_at, status, sale_channel_id)
VALUES ('sce1', NOW(), 'admin', NOW(), 'admin', '2025-09-01 23:59:59', 'evt1', '2025-08-01 00:00:00', 'ACTIVE', 'sc1'),
       ('sce2', NOW(), 'admin', NOW(), 'admin', '2025-08-30 23:59:59', 'evt1', '2025-08-10 00:00:00', 'ACTIVE', 'sc2'),
       ('sce3', NOW(), 'admin', NOW(), 'admin', '2025-09-15 23:59:59', 'evt2', '2025-08-05 00:00:00', 'INACTIVE',
        'sc3');

INSERT INTO salechannelorder (id, change_timestamp, change_user, create_timestamp, create_user, order_id, revenue,
                              sale_channel_id)
VALUES ('sco1', NOW(), 'admin', NOW(), 'admin', 'ord1001', 250.00, 'sc1'),
       ('sco2', NOW(), 'admin', NOW(), 'admin', 'ord1002', 120.00, 'sc1'),
       ('sco3', NOW(), 'admin', NOW(), 'admin', 'ord1003', 75.00, 'sc2'),
       ('sco4', NOW(), 'admin', NOW(), 'admin', 'ord1004', 500.00, 'sc3');
