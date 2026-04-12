ALTER TABLE organization
    ADD COLUMN code VARCHAR(255) NOT NULL DEFAULT '';

-- Update existing records with a unique code based on the organization name add _ between words and Org at the first
UPDATE organization
SET code = CONCAT('Org_', REPLACE(name, ' ', '_'))
WHERE code = '';