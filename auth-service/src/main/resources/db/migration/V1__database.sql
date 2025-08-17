CREATE TABLE IF NOT EXISTS organization
(
    id               VARCHAR(255) PRIMARY KEY,
    name             VARCHAR(255),
    description      TEXT,
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME
);

CREATE TABLE IF NOT EXISTS useraccount
(
    id               VARCHAR(255) PRIMARY KEY,
    name             VARCHAR(255),
    dob              DATETIME,
    phone_number     VARCHAR(255),
    user_name        VARCHAR(255) UNIQUE,
    password         VARCHAR(255),
    email            VARCHAR(255) UNIQUE,
    organization_id  VARCHAR(255),
    create_user      VARCHAR(255),
    change_user      VARCHAR(255),
    create_timestamp DATETIME,
    change_timestamp DATETIME,
    FOREIGN KEY (organization_id) REFERENCES organization (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS usergroup
(
    id                     VARCHAR(255) PRIMARY KEY,
    name                   VARCHAR(255),
    description            TEXT,
    administrator          BOOLEAN,
    organization_id        VARCHAR(255),
    user_group_permissions TEXT,
    create_user            VARCHAR(255),
    change_user            VARCHAR(255),
    create_timestamp       DATETIME,
    change_timestamp       DATETIME,
    FOREIGN KEY (organization_id) REFERENCES organization (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS userpermissions
(
    user_group_id   VARCHAR(255) NOT NULL,
    user_account_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_group_id, user_account_id),
    FOREIGN KEY (user_group_id) REFERENCES usergroup (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user_account_id) REFERENCES useraccount (id) ON DELETE CASCADE ON UPDATE CASCADE
);
