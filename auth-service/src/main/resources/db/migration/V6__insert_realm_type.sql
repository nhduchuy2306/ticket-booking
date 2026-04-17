ALTER TABLE useraccount
    ADD COLUMN realm_type VARCHAR(36) NOT NULL DEFAULT '';

-- Update existing records with a default realm type, for example, 'GYP_DEFAULT_REALM'
UPDATE useraccount
SET realm_type = 'GYP_DEFAULT_REALM'
WHERE realm_type = '';