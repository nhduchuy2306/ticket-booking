INSERT INTO category (id, name, description, create_user, create_timestamp)
VALUES ('cat1', 'Music Concerts', 'Live music performances', 'admin', NOW()),
       ('cat2', 'Sports', 'Sporting events and competitions', 'admin', NOW()),
       ('cat3', 'Theater', 'Drama and stage performances', 'admin', NOW()),
       ('cat4', 'Conferences', 'Business and professional events', 'admin', NOW());

INSERT INTO venue (id, name, address, city, country, capacity, latitude, longitude, create_user, create_timestamp)
VALUES ('ven1', 'Grand Arena', '123 Main Street', 'New York', 'USA', 20000, 40.7128, -74.0060, 'admin', NOW()),
       ('ven2', 'Olympic Stadium', '456 Sports Ave', 'London', 'UK', 80000, 51.5074, -0.1278, 'admin', NOW()),
       ('ven3', 'Royal Theater', '789 Arts Boulevard', 'Paris', 'France', 1500, 48.8566, 2.3522, 'admin', NOW());

INSERT INTO organizer (id, name, user_name, phone_number, create_user, create_timestamp)
VALUES ('org1', 'Live Nation', 'livenation', '+1234567890', 'admin', NOW()),
       ('org2', 'Sports International', 'sportsintl', '+4423456789', 'admin', NOW()),
       ('org3', 'Cultural Events Ltd', 'culturevents', '+3312345678', 'admin', NOW());

INSERT INTO event (id, name, description, status, start_time, end_time, door_open_time, door_close_time, organizer_id,
                   venue_id, create_user, create_timestamp)
VALUES ('evt1', 'Summer Music Festival', 'Annual outdoor music festival', 'PUBLISHED', '2023-07-15 18:00:00',
        '2023-07-15 23:00:00', '2023-07-15 16:00:00', '2023-07-15 22:30:00', 'org1', 'ven1', 'org1', NOW()),
       ('evt2', 'Championship Finals', 'National sports championship', 'PUBLISHED', '2023-08-20 19:00:00',
        '2023-08-20 21:30:00', '2023-08-20 17:00:00', '2023-08-20 20:30:00', 'org2', 'ven2', 'org2', NOW()),
       ('evt3', 'Hamlet Performance', 'Classic Shakespeare play', 'DRAFT', '2023-09-10 20:00:00', '2023-09-10 22:30:00',
        '2023-09-10 19:00:00', '2023-09-10 22:00:00', 'org3', 'ven3', 'org3', NOW());

INSERT INTO eventcategories (event_id, category_id)
VALUES ('evt1', 'cat1'),
       ('evt2', 'cat2'),
       ('evt3', 'cat3');

INSERT INTO tickettype (id, name, description, price, quantity_available, status, sale_start_date, sale_end_date,
                        event_id, create_user, create_timestamp)
VALUES ('tkt1', 'VIP Pass', 'Premium seating with backstage access', 299.99, 100, 'AVAILABLE', '2023-05-01 00:00:00',
        '2023-07-14 23:59:59', 'evt1', 'org1', NOW()),
       ('tkt2', 'General Admission', 'Standard festival entry', 89.99, 5000, 'AVAILABLE', '2023-05-01 00:00:00',
        '2023-07-14 23:59:59', 'evt1', 'org1', NOW()),
       ('tkt3', 'North Stand', 'Seating in north stand', 49.99, 2000, 'AVAILABLE', '2023-06-01 00:00:00',
        '2023-08-19 23:59:59', 'evt2', 'org2', NOW()),
       ('tkt4', 'Premium Seat', 'Center theater seating', 129.99, 100, 'COMING_SOON', '2023-07-01 00:00:00',
        '2023-09-09 23:59:59', 'evt3', 'org3', NOW());
INSERT INTO eventpromotion (id, code, discount_amount, valid_from, valid_to, event_id, create_user, create_timestamp)
VALUES ('promo1', 'EARLYBIRD20', 20.00, '2023-05-01 00:00:00', '2023-05-31 23:59:59', 'evt1', 'org1', NOW()),
       ('promo2', 'SUMMER10', 10.00, '2023-06-01 00:00:00', '2023-06-30 23:59:59', 'evt1', 'org1', NOW()),
       ('promo3', 'FANCLUB15', 15.00, '2023-06-15 00:00:00', '2023-07-10 23:59:59', 'evt2', 'org2', NOW());

INSERT INTO eventapproval (id, status, approved_by, approval_date, event_id, create_user, create_timestamp)
VALUES ('appr1', 'APPROVED', 'admin1', '2023-04-15 14:30:00', 'evt1', 'org1', NOW()),
       ('appr2', 'APPROVED', 'admin2', '2023-05-20 10:15:00', 'evt2', 'org2', NOW()),
       ('appr3', 'PENDING', NULL, NULL, 'evt3', 'org3', NOW());