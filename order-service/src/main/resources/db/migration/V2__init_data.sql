-- Insert sample orders
INSERT INTO orders (id, event_id, customer_email, status, total_amount, create_user, create_timestamp)
VALUES ('ecbf71ab-c11b-48cd-ba11-180a0775f23e', 'event-001', 'user1@example.com', 'PENDING', 200.00,
        'Admin', CURRENT_TIMESTAMP),
       ('c26e4e1b-a727-452f-beb1-427bb5c32edb', 'event-002', 'user2@example.com', 'PAID', 450.00,
        'Admin', CURRENT_TIMESTAMP);

-- Insert sample order details
INSERT INTO orderdetail (id, seat_id, price, quantity, order_id, create_user, create_timestamp)
VALUES ('f14c973a-9228-4c32-a863-c74094d9f29c', 'seat-A1', 100.00, 1, 'ecbf71ab-c11b-48cd-ba11-180a0775f23e',
        'Admin', CURRENT_TIMESTAMP),
       ('99b3f72c-c3e5-4bcf-976b-b500e1149d5b', 'seat-A2', 100.00, 1, 'ecbf71ab-c11b-48cd-ba11-180a0775f23e',
        'Admin', CURRENT_TIMESTAMP),
       ('f21a0d24-9988-4473-8bf9-42b1ccc438c0', 'seat-B1', 150.00, 2, 'c26e4e1b-a727-452f-beb1-427bb5c32edb',
        'Admin', CURRENT_TIMESTAMP),
       ('99593063-46eb-42eb-ba11-fa58ed34a96c', 'seat-B3', 150.00, 1, 'c26e4e1b-a727-452f-beb1-427bb5c32edb',
        'Admin', CURRENT_TIMESTAMP);
