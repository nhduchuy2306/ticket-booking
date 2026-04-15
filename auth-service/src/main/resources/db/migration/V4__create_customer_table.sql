CREATE TABLE IF NOT EXISTS customer (
    id                          VARCHAR(255) PRIMARY KEY,
    name                        VARCHAR(255),
    email                       VARCHAR(255) UNIQUE,
    phone_number                VARCHAR(255),
    dob                         DATETIME,

    provider                    VARCHAR(50)  NOT NULL DEFAULT 'local',
    provider_id                 VARCHAR(255),
    password                    VARCHAR(255),

    email_verified              BOOLEAN DEFAULT FALSE,
    email_verify_token          VARCHAR(255),
    email_verify_token_expiry   DATETIME,

    reset_password_token        VARCHAR(255),
    reset_password_token_expiry DATETIME,

    create_timestamp            DATETIME,
    change_timestamp            DATETIME,

    UNIQUE (provider, provider_id)
);