-- DEFAULT SIMPLE PASSWORD: 12345

-- Insert sample organizations
INSERT INTO organization (id, name, description, create_user, change_user, create_timestamp, change_timestamp)
VALUES ('1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f', 'My Organization', 'This is a sample organization', null, null,
        '2025-05-31 05:06:15', '2025-05-31 05:06:15'),
       ('2f3d4c5b-6a7b-8c9d-0e1f-2a3b4c5d6e7f', 'Another Org', 'This is another sample organization', null, null,
        '2025-05-31 05:06:15', '2025-05-31 05:06:15');

-- Insert sample user accounts
INSERT INTO useraccount (id, name, dob, phone_number, user_name, password, email, create_user, change_user,
                         create_timestamp, change_timestamp, organization_id)
VALUES ('0ead97fd-06e7-4977-a76e-b906c61964bb', 'fsdfsdf', '1985-05-15 08:00:00', '123123', 'sfdsdfsdf', null,
        'asdasd@gmail.com', null, null, '2025-06-01 04:51:33', '2025-06-01 04:51:33',
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('9caedd6b-0898-4921-ba2a-2c8149bd45d2', 'John Doe', '1985-05-15 08:00:00', '0987654321', 'john_doe', '123',
        'abc1@gmail.com', 'system', 'system', '2025-05-31 05:06:15', '2025-05-31 05:06:15',
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('9d39877d-f621-4b99-8107-b50dae5febd5', 'Alice Johnson', '2000-07-10 15:45:00', '0901234567', 'alice_johnson',
        '123', 'abc3@gmail.com', 'system', 'system', '2025-05-31 05:06:15', '2025-05-31 05:06:15',
        '2f3d4c5b-6a7b-8c9d-0e1f-2a3b4c5d6e7f'),
       ('ab05bf24-4dd1-4284-ac11-e74192098a39', 'Normal', '2000-07-10 15:45:00', '0901234567', 'Normal',
        '$2a$10$E97.WBjrwF4YuPo9Kd3A7emk9uNMkxBaGNFodm3IkNsSEEXRhZ162', 'abc4@gmail.com', 'system', 'system',
        '2025-05-31 05:06:15', '2025-05-31 05:06:15', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('d3a6a81c-5660-4869-abd0-afbf2cfade72', 'Organizer', '2000-07-10 15:45:00', '0901234567', 'Organizer',
        '$2a$10$aBCO7e10Tk6q8pahQkZnAOnVLAkFc87kXRYBg.cv8Z56evvpvUS0e', 'abc5@gmail.com', 'system', 'system',
        '2025-05-31 05:06:15', '2025-05-31 05:06:15', '2f3d4c5b-6a7b-8c9d-0e1f-2a3b4c5d6e7f'),
       ('dae4f91d-ed9a-4410-84bf-76a49af7cc41', '123123', '1985-05-15 08:00:00', '123123123', '123123123', null,
        '123123123123@gmail.com', null, null, '2025-06-01 04:44:04', '2025-06-01 04:44:04',
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('e76bf433-58ce-4d91-abd1-40bdb59bab9a', 'Admin', '2000-07-10 15:45:00', '0901234567', 'Admin',
        '$2a$10$M9nv8CucWsic2RtPBGToF.KM0Quv.cX2t/SVEhPX/TXWeYccZiEKK', 'abc6@gmail.com', 'system', 'system',
        '2025-05-31 05:06:15', '2025-05-31 05:06:15', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('f505442a-d7f9-4783-9ed9-2e33d0ef12eb', 'Jane Smith', '1990-03-22 12:30:00', '0912345678', 'jane_smith', '123',
        'abc2@gamil.com', 'system', 'system', '2025-05-31 05:06:15', '2025-05-31 05:06:15',
        '2f3d4c5b-6a7b-8c9d-0e1f-2a3b4c5d6e7f');

-- Insert sample user groups
INSERT INTO usergroup (id, name, description, administrator, user_group_permissions, create_user, change_user,
                       create_timestamp, change_timestamp, organization_id)
VALUES ('0432f3d0-5d41-42f2-b836-b44b9386e6b4', 'normal', 'This is Normal role', 0,
        '{"permissionItems":[{"actions":["READ"],"applicationId":"app.user.account"},{"actions":["READ","DELETE","CREATE","UPDATE"],"applicationId":"app.event"},{"actions":["READ","DELETE","CREATE","UPDATE"],"applicationId":"app.user.group"},{"actions":["READ","DELETE","EXPORT","CREATE","IMPORT","UPDATE"],"applicationId":"app.configuration"}]}',
        null, null, null, null, '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('0f044715-1926-4f3c-9e7b-563200c4d9cc', 'UserGroup2', 'Editors group with limited permissions', 0,
        '{"permissionItems":[{"actions":["READ"],"applicationId":"app.user.group"},{"actions":["LOGIN","READ","LOGOUT","DELETE","CREATE","UPDATE"],"applicationId":"app.user.account"},{"actions":["READ","DELETE","CREATE","UPDATE"],"applicationId":"app.event"}]}',
        'system', 'system', '2025-05-31 05:06:15', '2025-05-31 05:06:15', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('287e90bc-d761-4fbf-9b05-673d5da53c87', 'ROOT', 'This is Admin role', 1, '{}', null, null, null, null,
        '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('7ac08049-1709-4757-9250-bcf16c32f57a', 'UserGroup3', 'Viewers group with view-only permissions', 0,
        '{"permissionItems":[{"applicationId":"app.user.group","actions":["READ"]},{"applicationId":"app.user.account","actions":["READ","CREATE"]}]}',
        'system', 'system', '2025-05-31 05:06:15', '2025-05-31 05:06:15', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('7b4b7969-3544-433c-bec5-40ee41d38384', 'Organizer1', 'This is Organizer role', 0,
        '{"permissionItems":[{"actions":["READ"],"applicationId":"app.user.account"},{"actions":["DELETE","READ","CREATE","UPDATE"],"applicationId":"app.event"}]}',
        null, null, null, null, '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f'),
       ('cb2f0983-5b96-4d4d-8e61-12286d75ac54', 'UserGroup1', 'Viewer', 0,
        '{"permissionItems":[{"applicationId":"app.user.group","actions":["CREATE","UPDATE","READ","DELETE"]}]}',
        'system', 'system', '2025-05-31 05:06:15', '2025-05-31 05:06:15', '1f2d3c4b-5a6b-7c8d-9e0f-1a2b3c4d5e6f');

-- Establish relationships between user accounts and user groups
INSERT INTO userpermissions (user_group_id, user_account_id)
VALUES ('0432f3d0-5d41-42f2-b836-b44b9386e6b4', '0ead97fd-06e7-4977-a76e-b906c61964bb'),
       ('cb2f0983-5b96-4d4d-8e61-12286d75ac54', '9caedd6b-0898-4921-ba2a-2c8149bd45d2'),
       ('7ac08049-1709-4757-9250-bcf16c32f57a', '9d39877d-f621-4b99-8107-b50dae5febd5'),
       ('0432f3d0-5d41-42f2-b836-b44b9386e6b4', 'ab05bf24-4dd1-4284-ac11-e74192098a39'),
       ('7b4b7969-3544-433c-bec5-40ee41d38384', 'd3a6a81c-5660-4869-abd0-afbf2cfade72'),
       ('0432f3d0-5d41-42f2-b836-b44b9386e6b4', 'dae4f91d-ed9a-4410-84bf-76a49af7cc41'),
       ('287e90bc-d761-4fbf-9b05-673d5da53c87', 'e76bf433-58ce-4d91-abd1-40bdb59bab9a'),
       ('0f044715-1926-4f3c-9e7b-563200c4d9cc', 'f505442a-d7f9-4783-9ed9-2e33d0ef12eb');

