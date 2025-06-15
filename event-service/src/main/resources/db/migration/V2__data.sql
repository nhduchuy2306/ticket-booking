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

INSERT INTO venuemap (id, name, width, height, venue_id, create_user, create_timestamp)
VALUES ('2a941d3d-6726-49f4-9622-effa14492dde', 'Grand Arena Main Map', 100.0, 80.0, '24743759-bc98-45b2-9f3d-63fb2e467fd9', 'admin', NOW()),
       ('0ce83fc4-7155-4fb9-b614-d2081ceb293e', 'Olympic Stadium Main Map', 200.0, 150.0, 'ad17fc7f-1514-47ec-a9fa-9c7a5ebefd12', 'admin', NOW()),
       ('bfc4eceb-0f3c-4576-a399-6a44a5570099', 'Royal Theater Main Map', 60.0, 60.0, '23884001-0cc9-46e9-95d2-82f5e64dcf68', 'admin', NOW());

INSERT INTO seatmap (id, name, venue_type, seat_config, stage_config, venue_map_id, create_user, create_timestamp)
VALUES ('c143797f-4b03-4d21-aa4a-02a21f195b55',
        'Main Hall Seating',
        'RECTANGLE',
        '{"id":"9c9c6f4a-8834-46b8-a373-35dc1dea945f","name":"My dinh","type":"SEATED","position":{"x":10.0,"y":10.0},"dimensions":null,"rotation":0.0,"capacity":0,"priceCategory":null,"rows":[{"id":"40cb9c94-357b-41e2-ab4f-6ac257b7ebdd","name":"A","position":{"x":0.0,"y":0.0},"seats":[{"id":"f446cf1b-3d77-42d9-9283-e6e78507e471","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"adc3adf9-fd95-4d60-b137-d78248d728ed","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"be4c322e-7aaa-4314-b94b-57d59c76b24f","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"69f4722f-d3ea-4363-b5dc-5b4f87b7cb8f","name":"B","position":{"x":0.0,"y":5.0},"seats":[{"id":"1b85b4d9-d85f-4231-8f07-af3c117769fd","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"0696b382-02bb-4b0c-b2a0-512b9415ba1f","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"dc5347a8-e30d-4cd1-bd17-67bbdbec8569","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"0a2cfe9f-90f0-4072-bc3f-98467e343cde","name":"C","position":{"x":0.0,"y":10.0},"seats":[{"id":"22194980-fd14-4a26-b3f7-94314614df8b","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"7050c745-4d5f-44c6-9925-bb5d05200622","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"a990b372-3bf1-4e7b-9776-52d448e8d662","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"f0d3023b-39f4-4dc3-9912-dd659d13577d","name":"D","position":{"x":0.0,"y":15.0},"seats":[{"id":"3998167d-7982-45d0-a614-632ccc5fa4f8","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"667fe704-e26e-4808-9cc8-dc9b565fe377","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"998f5464-dc6f-4284-9866-1b93a2cebcf0","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"eb70495c-b852-4811-b595-626f9f5669be","name":"E","position":{"x":0.0,"y":20.0},"seats":[{"id":"4dbc6a68-c348-4b3b-95f3-88df46f804cc","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"260be55b-a685-4e87-af2e-b3eb138e4e68","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"3153fa77-e7cb-4563-8fc9-eecaa5b4186a","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3}],"tables":[],"arcProperties":null,"arc":false}',
        '{"id":"5361d1c9-3da4-44b7-82f4-aa8500b58112","name":"Stage1","position":{"x":0.0,"y":0.0},"dimensions":{"width":20.0,"height":10.0},"rotation":0.0,"shape":"SEMICIRCLE","orientation":"NORTH","description":null,"customVertices":[],"elevation":0.0,"active":true,"centerPosition":{"x":10.0,"y":5.0},"boundaryPoints":[{"x":15.0,"y":10.0},{"x":14.92403876506104,"y":9.131759111665348},{"x":14.698463103929543,"y":8.289899283371657},{"x":14.330127018922195,"y":7.5},{"x":13.83022221559489,"y":6.786061951567303},{"x":13.213938048432697,"y":6.16977778440511},{"x":12.5,"y":5.669872981077807},{"x":11.710100716628345,"y":5.301536896070458},{"x":10.868240888334652,"y":5.07596123493896},{"x":10.0,"y":5.0},{"x":9.131759111665348,"y":5.07596123493896},{"x":8.289899283371657,"y":5.301536896070457},{"x":7.500000000000001,"y":5.669872981077806},{"x":6.786061951567303,"y":6.16977778440511},{"x":6.1697777844051105,"y":6.786061951567302},{"x":5.669872981077806,"y":7.5},{"x":5.301536896070458,"y":8.289899283371655},{"x":5.07596123493896,"y":9.131759111665348},{"x":5.0,"y":10.0}]}',
        '2a941d3d-6726-49f4-9622-effa14492dde',
        'admin_user',
        NOW()),
       ('346c98b0-e754-4d34-9235-37e4099b6e43',
        'Circular Theater',
        'ROUND',
        '{"id":"9c9c6f4a-8834-46b8-a373-35dc1dea945f","name":"My dinh","type":"SEATED","position":{"x":10.0,"y":10.0},"dimensions":null,"rotation":0.0,"capacity":0,"priceCategory":null,"rows":[{"id":"40cb9c94-357b-41e2-ab4f-6ac257b7ebdd","name":"A","position":{"x":0.0,"y":0.0},"seats":[{"id":"f446cf1b-3d77-42d9-9283-e6e78507e471","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"adc3adf9-fd95-4d60-b137-d78248d728ed","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"be4c322e-7aaa-4314-b94b-57d59c76b24f","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"69f4722f-d3ea-4363-b5dc-5b4f87b7cb8f","name":"B","position":{"x":0.0,"y":5.0},"seats":[{"id":"1b85b4d9-d85f-4231-8f07-af3c117769fd","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"0696b382-02bb-4b0c-b2a0-512b9415ba1f","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"dc5347a8-e30d-4cd1-bd17-67bbdbec8569","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"0a2cfe9f-90f0-4072-bc3f-98467e343cde","name":"C","position":{"x":0.0,"y":10.0},"seats":[{"id":"22194980-fd14-4a26-b3f7-94314614df8b","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"7050c745-4d5f-44c6-9925-bb5d05200622","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"a990b372-3bf1-4e7b-9776-52d448e8d662","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"f0d3023b-39f4-4dc3-9912-dd659d13577d","name":"D","position":{"x":0.0,"y":15.0},"seats":[{"id":"3998167d-7982-45d0-a614-632ccc5fa4f8","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"667fe704-e26e-4808-9cc8-dc9b565fe377","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"998f5464-dc6f-4284-9866-1b93a2cebcf0","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3},{"id":"eb70495c-b852-4811-b595-626f9f5669be","name":"E","position":{"x":0.0,"y":20.0},"seats":[{"id":"4dbc6a68-c348-4b3b-95f3-88df46f804cc","name":"1","position":{"x":0.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"260be55b-a685-4e87-af2e-b3eb138e4e68","name":"2","position":{"x":5.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null},{"id":"3153fa77-e7cb-4563-8fc9-eecaa5b4186a","name":"3","position":{"x":10.0,"y":0.0},"status":null,"attributes":{},"useAbsolutePosition":false,"absolutePosition":null}],"arcProperties":null,"arc":false,"capacity":3}],"tables":[],"arcProperties":null,"arc":false}',
        '{"id":"5361d1c9-3da4-44b7-82f4-aa8500b58112","name":"Stage1","position":{"x":0.0,"y":0.0},"dimensions":{"width":20.0,"height":10.0},"rotation":0.0,"shape":"SEMICIRCLE","orientation":"NORTH","description":null,"customVertices":[],"elevation":0.0,"active":true,"centerPosition":{"x":10.0,"y":5.0},"boundaryPoints":[{"x":15.0,"y":10.0},{"x":14.92403876506104,"y":9.131759111665348},{"x":14.698463103929543,"y":8.289899283371657},{"x":14.330127018922195,"y":7.5},{"x":13.83022221559489,"y":6.786061951567303},{"x":13.213938048432697,"y":6.16977778440511},{"x":12.5,"y":5.669872981077807},{"x":11.710100716628345,"y":5.301536896070458},{"x":10.868240888334652,"y":5.07596123493896},{"x":10.0,"y":5.0},{"x":9.131759111665348,"y":5.07596123493896},{"x":8.289899283371657,"y":5.301536896070457},{"x":7.500000000000001,"y":5.669872981077806},{"x":6.786061951567303,"y":6.16977778440511},{"x":6.1697777844051105,"y":6.786061951567302},{"x":5.669872981077806,"y":7.5},{"x":5.301536896070458,"y":8.289899283371655},{"x":5.07596123493896,"y":9.131759111665348},{"x":5.0,"y":10.0}]}',
        '0ce83fc4-7155-4fb9-b614-d2081ceb293e',
        'admin_user',
        NOW());

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