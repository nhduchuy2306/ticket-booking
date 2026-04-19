-- Add hold token support to orders
ALTER TABLE orders
    ADD COLUMN hold_token VARCHAR(255) NULL;

