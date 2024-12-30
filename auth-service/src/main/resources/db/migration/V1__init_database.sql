-- Create `useraccount` table
CREATE TABLE IF NOT EXISTS useraccount
(
    id               VARCHAR(255) PRIMARY KEY,
    name             VARCHAR(255),
    dob              DATETIME,
    phone_number     VARCHAR(255),
    user_name        VARCHAR(255) UNIQUE,
    password         VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

-- Create `usergroup` table
CREATE TABLE IF NOT EXISTS usergroup
(
    id                     VARCHAR(255) PRIMARY KEY,
    name                   VARCHAR(255),
    description            TEXT,
    administrator          BOOLEAN,
    user_group_permissions TEXT,
    create_user            VARCHAR(255),
    change_user            VARCHAR(255),
    create_timestamp       DATETIME,
    change_timestamp       DATETIME
);

-- Create join table for the many-to-many relationship
CREATE TABLE IF NOT EXISTS userpermissions
(
    user_group_id   VARCHAR(255) NOT NULL,
    user_account_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_group_id, user_account_id),
    FOREIGN KEY (user_group_id) REFERENCES usergroup (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_account_id) REFERENCES useraccount (id) ON DELETE CASCADE ON UPDATE CASCADE
);
