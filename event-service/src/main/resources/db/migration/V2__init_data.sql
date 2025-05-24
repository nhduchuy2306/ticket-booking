INSERT INTO category (id, name, description, create_user, create_timestamp)
VALUES ('9e9c4b0a-7ccf-427d-975d-0a3046a7dba5', 'Music Concerts', 'Live music performances', 'admin', NOW()),
       ('1d35349f-b13a-49c5-b9b5-132aeb45f6fa', 'Sports', 'Sporting events and competitions', 'admin', NOW()),
       ('b21c6cb3-2665-4d2d-a041-ce0809ad399a', 'Theater', 'Drama and stageConfig performances', 'admin', NOW()),
       ('ab3482ec-d131-43f7-a4b5-b5b81b6714a5', 'Conferences', 'Business and professional events', 'admin', NOW());

INSERT INTO venue (id, name, address, city, country, capacity, latitude, longitude, create_user, create_timestamp)
VALUES ('24743759-bc98-45b2-9f3d-63fb2e467fd9', 'Grand Arena', '123 Main Street', 'New York', 'USA', 20000, 40.7128,
        -74.0060, 'admin', NOW()),
       ('ad17fc7f-1514-47ec-a9fa-9c7a5ebefd12', 'Olympic Stadium', '456 Sports Ave', 'London', 'UK', 80000, 51.5074,
        -0.1278, 'admin', NOW()),
       ('23884001-0cc9-46e9-95d2-82f5e64dcf68', 'Royal Theater', '789 Arts Boulevard', 'Paris', 'France', 1500, 48.8566,
        2.3522, 'admin', NOW());

INSERT INTO organizer (id, name, user_name, phone_number, create_user, create_timestamp)
VALUES ('cd79cb00-3978-4709-81fc-9aeb41b0a430', 'Live Nation', 'livenation', '+1234567890', 'admin', NOW()),
       ('be4d36d6-1f61-45ff-8c94-75d20d4ec1dd', 'Sports International', 'sportsintl', '+4423456789', 'admin', NOW()),
       ('471b4a5b-7bfa-4d12-a0d2-d3f0021fba4a', 'Cultural Events Ltd', 'culturevents', '+3312345678', 'admin', NOW());

INSERT INTO event (id, name, description, status, start_time, end_time, door_open_time, door_close_time, organizer_id,
                   venue_id, create_user, create_timestamp)
VALUES ('33fe6f8c-7c73-4248-99b6-5a7934af2905', 'Summer Music Festival', 'Annual outdoor music festival', 'PUBLISHED',
        '2023-07-15 18:00:00',
        '2023-07-15 23:00:00', '2023-07-15 16:00:00', '2023-07-15 22:30:00', 'cd79cb00-3978-4709-81fc-9aeb41b0a430',
        '24743759-bc98-45b2-9f3d-63fb2e467fd9', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW()),
       ('63fcf9a1-3b6a-40be-a277-f7c91092ee52', 'Championship Finals', 'National sports championship', 'PUBLISHED',
        '2023-08-20 19:00:00',
        '2023-08-20 21:30:00', '2023-08-20 17:00:00', '2023-08-20 20:30:00', 'be4d36d6-1f61-45ff-8c94-75d20d4ec1dd',
        'ad17fc7f-1514-47ec-a9fa-9c7a5ebefd12', 'be4d36d6-1f61-45ff-8c94-75d20d4ec1dd', NOW()),
       ('98ff9939-f15f-490a-89cb-a276c7537b91', 'Hamlet Performance', 'Classic Shakespeare play', 'DRAFT',
        '2023-09-10 20:00:00', '2023-09-10 22:30:00',
        '2023-09-10 19:00:00', '2023-09-10 22:00:00', '471b4a5b-7bfa-4d12-a0d2-d3f0021fba4a',
        '23884001-0cc9-46e9-95d2-82f5e64dcf68', '471b4a5b-7bfa-4d12-a0d2-d3f0021fba4a', NOW());

INSERT INTO eventcategories (event_id, category_id)
VALUES ('33fe6f8c-7c73-4248-99b6-5a7934af2905', '9e9c4b0a-7ccf-427d-975d-0a3046a7dba5'),
       ('63fcf9a1-3b6a-40be-a277-f7c91092ee52', '1d35349f-b13a-49c5-b9b5-132aeb45f6fa'),
       ('98ff9939-f15f-490a-89cb-a276c7537b91', 'b21c6cb3-2665-4d2d-a041-ce0809ad399a');

INSERT INTO tickettype (id, name, description, price, color, quantity_available, status, sale_start_date, sale_end_date,
                        event_id, create_user, create_timestamp)
VALUES ('fb81dfc2-1fda-481a-968c-88c589e4c380', 'VIP Pass', 'Premium seating with backstage access', 299.99, '#0fff',
        100,
        'AVAILABLE', '2023-05-01 00:00:00',
        '2023-07-14 23:59:59', '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW()),
       ('8f58d410-c3a3-49d9-9b4d-2620b593e1e2', 'General Admission', 'Standard festival entry', 89.99, '#0fff', 5000,
        'AVAILABLE', '2023-05-01 00:00:00',
        '2023-07-14 23:59:59', '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW()),
       ('f4ae1ab5-90ad-4505-858c-cc0502a8b819', 'North Stand', 'Seating in north stand', 49.99, '#0fff', 2000,
        'AVAILABLE',
        '2023-06-01 00:00:00',
        '2023-08-19 23:59:59', '63fcf9a1-3b6a-40be-a277-f7c91092ee52', 'be4d36d6-1f61-45ff-8c94-75d20d4ec1dd', NOW()),
       ('e6fff39d-d99d-43b0-8faf-f568078d4120', 'Premium Seat', 'Center theater seating', 129.99, '#0fff', 100,
        'COMING_SOON',
        '2023-07-01 00:00:00',
        '2023-09-09 23:59:59', '98ff9939-f15f-490a-89cb-a276c7537b91', '471b4a5b-7bfa-4d12-a0d2-d3f0021fba4a', NOW());

INSERT INTO eventpromotion (id, code, discount_amount, valid_from, valid_to, event_id, create_user, create_timestamp)
VALUES ('9aaf9834-3892-4280-a895-aaa165c097f9', 'EARLYBIRD20', 20.00, '2023-05-01 00:00:00', '2023-05-31 23:59:59',
        '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW()),
       ('14f4bcec-8861-4691-9d73-bf12bf1aed45', 'SUMMER10', 10.00, '2023-06-01 00:00:00', '2023-06-30 23:59:59',
        '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW()),
       ('efda1945-f830-4fcf-b4c9-62f21daa4cf0', 'FANCLUB15', 15.00, '2023-06-15 00:00:00', '2023-07-10 23:59:59',
        '63fcf9a1-3b6a-40be-a277-f7c91092ee52', 'be4d36d6-1f61-45ff-8c94-75d20d4ec1dd', NOW());

INSERT INTO eventapproval (id, status, approved_by, approval_date, event_id, create_user, create_timestamp)
VALUES ('b55928ed-b058-4349-9c5c-ceeaabf2fd1e', 'APPROVED', 'admin1', '2023-04-15 14:30:00',
        '33fe6f8c-7c73-4248-99b6-5a7934af2905', 'cd79cb00-3978-4709-81fc-9aeb41b0a430', NOW()),
       ('528f5970-e756-4e72-9357-1ba6e208e26a', 'APPROVED', 'admin2', '2023-05-20 10:15:00',
        '63fcf9a1-3b6a-40be-a277-f7c91092ee52', 'be4d36d6-1f61-45ff-8c94-75d20d4ec1dd', NOW()),
       ('e6b3632a-4220-4c43-82e4-478666d38dd5', 'PENDING', NULL, NULL, '98ff9939-f15f-490a-89cb-a276c7537b91',
        '471b4a5b-7bfa-4d12-a0d2-d3f0021fba4a', NOW());