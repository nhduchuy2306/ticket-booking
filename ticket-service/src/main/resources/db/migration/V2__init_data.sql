-- Insert sample TICKETTYPE data
INSERT INTO tickettype (id, name, description, price, color, quantity_available, status, sale_start_date, sale_end_date,
                        event_id, create_user, create_timestamp, organization_id)
VALUES ('fb81dfc2-1fda-481a-968c-88c589e4c380', 'VIP Pass', 'Premium seating with backstage access', 299.99, '#0fff',
        100,
        'AVAILABLE', '2023-05-01 00:00:00',
        '2023-07-14 23:59:59', '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW(),
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('8f58d410-c3a3-49d9-9b4d-2620b593e1e2', 'General Admission', 'Standard festival entry', 89.99, '#0fff', 5000,
        'AVAILABLE', '2023-05-01 00:00:00',
        '2023-07-14 23:59:59', '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW(),
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('f4ae1ab5-90ad-4505-858c-cc0502a8b819', 'North Stand', 'Seating in north stand', 49.99, '#0fff', 2000,
        'AVAILABLE',
        '2023-06-01 00:00:00',
        '2023-08-19 23:59:59', '63fcf9a1-3b6a-40be-a277-f7c91092ee52', 'be4d36d6-1f61-45ff-8c94-75d20d4ec1dd', NOW(),
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('e6fff39d-d99d-43b0-8faf-f568078d4120', 'Premium Seat', 'Center theater seating', 129.99, '#0fff', 100,
        'COMING_SOON',
        '2023-07-01 00:00:00',
        '2023-09-09 23:59:59', '98ff9939-f15f-490a-89cb-a276c7537b91', '471b4a5b-7bfa-4d12-a0d2-d3f0021fba4a', NOW(),
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f');

-- Insert sample TICKET data
INSERT INTO ticket (id, ticket_code, ticket_type_id, event_id, event_name, event_date_time, reserved_date,
                    seat_info, seat_id, status, attendee_name, attendee_email, qr_code_url, pdf_url,
                    create_user, change_user, create_timestamp, change_timestamp)
VALUES ('tk1', 'TICKET-0001', '8f58d410-c3a3-49d9-9b4d-2620b593e1e2', 'event-001', 'Spring Concert 2025',
        '2025-06-10 19:00:00', NULL,
        'A1', 'A1', 'AVAILABLE', NULL, NULL, NULL, NULL,
        'system', 'system', NOW(), NOW()),

       ('tk2', 'TICKET-0002', '8f58d410-c3a3-49d9-9b4d-2620b593e1e2', 'event-001', 'Spring Concert 2025',
        '2025-06-10 19:00:00', NULL,
        'A2', 'A2', 'AVAILABLE', NULL, NULL, NULL, NULL,
        'system', 'system', NOW(), NOW()),

       ('tk3', 'TICKET-0003', 'fb81dfc2-1fda-481a-968c-88c589e4c380', 'event-001', 'Spring Concert 2025',
        '2025-06-10 19:00:00', NULL,
        'VIP-1', 'VIP-1', 'AVAILABLE', NULL, NULL, NULL, NULL,
        'system', 'system', NOW(), NOW());
-- Insert sample TICKETORDERLINK data
INSERT INTO ticketorderlink (id, ticket_id, order_id, purchase_time, price_at_purchase,
                             create_user, change_user, create_timestamp, change_timestamp)
VALUES ('tk-order-1', 'tk1', 'order-001', NOW(), 20.00,
        'system', 'system', NOW(), NOW());
