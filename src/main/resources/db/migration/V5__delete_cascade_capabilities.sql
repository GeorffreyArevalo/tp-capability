ALTER TABLE bootcamp_capability
DROP CONSTRAINT fk_bootcamp_capability_capability;

ALTER TABLE bootcamp_capability
    ADD CONSTRAINT fk_bootcamp_capability_capability
        FOREIGN KEY (capability_id) REFERENCES capability(id)
            ON DELETE CASCADE;