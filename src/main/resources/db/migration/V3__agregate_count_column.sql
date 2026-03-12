ALTER TABLE capability
    ADD COLUMN IF NOT EXISTS technology_count INTEGER;

UPDATE capability
SET technology_count = 0
WHERE technology_count IS NULL;

ALTER TABLE capability
    ALTER COLUMN technology_count SET DEFAULT 0;

ALTER TABLE capability
    ALTER COLUMN technology_count SET NOT NULL;
