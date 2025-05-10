-- Insert sample TICKETTYPE data
INSERT INTO tickettype (id, event_id, name, description, price, quantity_available, sale_start_date,
                        sale_end_date, create_user, change_user, create_timestamp, change_timestamp)
VALUES ('type-std-001', 'event-001', 'Standard', 'Standard seating ticket', 20.00, 100,
        NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'system', 'system', NOW(), NOW()),
       ('type-vip-001', 'event-001', 'VIP', 'VIP ticket with premium seating and lounge access', 50.00, 20,
        NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'system', 'system', NOW(), NOW());

-- Insert sample TICKET data
-- Insert sample TICKET data (updated)
INSERT INTO ticket (id, event_id, event_name, ticket_type_id, event_date_time,
                    reserved_date, create_user, change_user, create_timestamp, change_timestamp)
VALUES ('tk1', 'event-001', 'Spring Concert 2025', 'type-std-001', '2025-06-10 19:00:00',
        NULL, 'system', 'system', NOW(), NOW()),
       ('tk2', 'event-001', 'Spring Concert 2025', 'type-std-001', '2025-06-10 19:00:00',
        NULL, 'system', 'system', NOW(), NOW()),
       ('tk3', 'event-001', 'Spring Concert 2025', 'type-vip-001', '2025-06-10 19:00:00',
        NULL, 'system', 'system', NOW(), NOW());

-- Insert sample TICKETGENERATION data
INSERT INTO ticketgeneration (id, event_id, event_name, ticket_id, seat_info, ticket_number,
                              attendee_name, attendee_email, event_date_time, status,
                              create_user, change_user, create_timestamp, change_timestamp)
VALUES ('tk-gen-1', 'event-001', 'Spring Concert 2025', 'tk1', 'A1', 'TICKET-0001',
        NULL, NULL, '2025-06-10 19:00:00', 'AVAILABLE',
        'system', 'system', NOW(), NOW()),
       ('tk-gen-2', 'event-001', 'Spring Concert 2025', 'tk2', 'A2', 'TICKET-0002',
        NULL, NULL, '2025-06-10 19:00:00', 'AVAILABLE',
        'system', 'system', NOW(), NOW()),
       ('tk-gen-3', 'event-001', 'Spring Concert 2025', 'tk3', 'VIP-1', 'TICKET-0003',
        NULL, NULL, '2025-06-10 19:00:00', 'AVAILABLE',
        'system', 'system', NOW(), NOW());

-- Only if a ticket has been purchased
INSERT INTO ticketorderlink (id, ticket_id, order_id, purchase_time, create_user, change_user, create_timestamp,
                             change_timestamp)
VALUES ('tk-order-1', 'tk1', 'order-001', NOW(), 'system', 'system', NOW(), NOW());
