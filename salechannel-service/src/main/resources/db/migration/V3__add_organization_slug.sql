ALTER TABLE salechannel
	ADD COLUMN IF NOT EXISTS organization_slug VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_salechannel_organization_slug_type
	ON salechannel (organization_slug, channel_type);

