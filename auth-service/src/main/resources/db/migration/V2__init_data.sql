-- Insert sample user accounts
INSERT INTO useraccount (id, name, dob, phone_number, create_user, change_user, create_timestamp,
                         change_timestamp, user_name, password, email)
VALUES ('9caedd6b-0898-4921-ba2a-2c8149bd45d2', 'John Doe', '1985-05-15 08:00:00', '0987654321', 'system', 'system',
        NOW(), NOW(), 'john_doe', '123', 'abc1@gmail.com'),
       ('f505442a-d7f9-4783-9ed9-2e33d0ef12eb', 'Jane Smith', '1990-03-22 12:30:00', '0912345678', 'system',
        'system', NOW(), NOW(), 'jane_smith', '123', 'abc2@gamil.com'),
       ('9d39877d-f621-4b99-8107-b50dae5febd5', 'Alice Johnson', '2000-07-10 15:45:00', '0901234567', 'system',
        'system', NOW(), NOW(), 'alice_johnson', '123', 'abc3@gmail.com');

-- Insert sample user groups
INSERT INTO usergroup (id, name, description, administrator, user_group_permissions, create_user,
                       change_user, create_timestamp, change_timestamp)
VALUES ('cb2f0983-5b96-4d4d-8e61-12286d75ac54', 'UserGroup1', 'Viewer', FALSE,
        '{"permissionItems":[{"applicationId":"app.user","actions":["CREATE","UPDATE","READ","DELETE"],"uuid":"70ac16be-aeae-4ded-b7db-79d20e9ffe24"}]}',
        'system', 'system', NOW(), NOW()),
       ('0f044715-1926-4f3c-9e7b-563200c4d9cc', 'UserGroup2', 'Editors group with limited permissions', FALSE,
        '{"permissionItems":[{"applicationId":"app.user","actions":["READ"],"uuid":"70ac16be-aeae-4ded-b7db-79d20e9ffe24"}]}',
        'system', 'system', NOW(), NOW()),
       ('7ac08049-1709-4757-9250-bcf16c32f57a', 'UserGroup3', 'Viewers group with view-only permissions', FALSE,
        '{"permissionItems":[{"applicationId":"app.user","actions":["READ"],"uuid":"70ac16be-aeae-4ded-b7db-79d20e9ffe24"},{"applicationId":"app.movie","actions":["READ","CREATE"],"uuid":"70ac16be-aeae-4ded-b7db-79d20e9ffe24"}]}',
        'system', 'system', NOW(), NOW());

-- Establish relationships between user accounts and user groups
INSERT INTO userpermissions (user_group_id, user_account_id)
VALUES ('cb2f0983-5b96-4d4d-8e61-12286d75ac54', '9caedd6b-0898-4921-ba2a-2c8149bd45d2'),
       ('0f044715-1926-4f3c-9e7b-563200c4d9cc', 'f505442a-d7f9-4783-9ed9-2e33d0ef12eb'),
       ('7ac08049-1709-4757-9250-bcf16c32f57a', '9d39877d-f621-4b99-8107-b50dae5febd5');
