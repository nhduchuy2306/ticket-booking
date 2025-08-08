-- Insert sample TICKETTYPE data
INSERT INTO tickettype (id, event_id, name, description, price, quantity_total, quantity_available,
                        sale_start_date, sale_end_date,
                        create_user, change_user, create_timestamp, change_timestamp)
VALUES ('type-std-001', 'event-001', 'Standard', 'Standard seating ticket', 20.00, 100, 100,
        NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY),
        'system', 'system', NOW(), NOW()),

       ('type-vip-001', 'event-001', 'VIP', 'VIP ticket with premium seating and lounge access', 50.00, 20, 20,
        NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY),
        'system', 'system', NOW(), NOW());

-- Insert sample TICKET data
INSERT INTO ticket (id, ticket_code, ticket_type_id, event_id, event_name, event_date_time, reserved_date,
                    seat_info, status, attendee_name, attendee_email, qr_code_url, pdf_url,
                    create_user, change_user, create_timestamp, change_timestamp)
VALUES ('tk1', 'TICKET-0001', 'type-std-001', 'event-001', 'Spring Concert 2025', '2025-06-10 19:00:00', NULL,
        'A1', 'AVAILABLE', NULL, NULL, NULL, NULL,
        'system', 'system', NOW(), NOW()),

       ('tk2', 'TICKET-0002', 'type-std-001', 'event-001', 'Spring Concert 2025', '2025-06-10 19:00:00', NULL,
        'A2', 'AVAILABLE', NULL, NULL, NULL, NULL,
        'system', 'system', NOW(), NOW()),

       ('tk3', 'TICKET-0003', 'type-vip-001', 'event-001', 'Spring Concert 2025', '2025-06-10 19:00:00', NULL,
        'VIP-1', 'AVAILABLE', NULL, NULL, NULL, NULL,
        'system', 'system', NOW(), NOW());

-- Insert sample TICKETORDERLINK data
INSERT INTO ticketorderlink (id, ticket_id, order_id, purchase_time, price_at_purchase,
                             create_user, change_user, create_timestamp, change_timestamp)
VALUES ('tk-order-1', 'tk1', 'order-001', NOW(), 20.00,
        'system', 'system', NOW(), NOW());
