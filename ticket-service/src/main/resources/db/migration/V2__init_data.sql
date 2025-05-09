-- Insert sample TICKETTYPE data
INSERT INTO tickettype (id, event_id, name, description, price, quantity_available,
                        sale_start_date, sale_end_date, create_user, change_user, create_timestamp, change_timestamp)
VALUES ('type-std-001', 'event-001', 'Standard', 'Standard seating ticket', 20.00, 100,
        NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'system', 'system', NOW(), NOW()),
       ('type-vip-001', 'event-001', 'VIP', 'VIP ticket with premium seating and lounge access', 50.00, 20,
        NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'system', 'system', NOW(), NOW());

-- Insert sample TICKET data
INSERT INTO ticket (id, event_id, event_name, seat_info, ticket_type_id, ticket_number, attendee_name,
                    attendee_email, event_date_time, status, reserved_date, create_user, change_user,
                    create_timestamp, change_timestamp)
VALUES (UUID(), 'event-001', 'Spring Concert 2025', 'A1', 'type-std-001', 'TICKET-0001', NULL,
        NULL, '2025-06-10 19:00:00', 'AVAILABLE', NULL, 'system', 'system', NOW(), NOW()),
       (UUID(), 'event-001', 'Spring Concert 2025', 'A2', 'type-std-001', 'TICKET-0002', NULL,
        NULL, '2025-06-10 19:00:00', 'AVAILABLE', NULL, 'system', 'system', NOW(), NOW()),
       (UUID(), 'event-001', 'Spring Concert 2025', 'VIP-1', 'type-vip-001', 'TICKET-0003', NULL,
        NULL, '2025-06-10 19:00:00', 'AVAILABLE', NULL, 'system', 'system', NOW(), NOW());

-- Insert sample TICKET_ORDER_LINK (optional)
-- Only if a ticket has been purchased
INSERT INTO ticketorderlink (id, ticket_id, order_id, purchase_time, create_user, change_user, create_timestamp,
                             change_timestamp)
VALUES (UUID(), 'TICKET-0001', 'order-001', NOW(), 'system', 'system', NOW(), NOW());
