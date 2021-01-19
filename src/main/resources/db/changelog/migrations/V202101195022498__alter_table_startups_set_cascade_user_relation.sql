ALTER TABLE startups DROP CONSTRAINT startups_owner_id_fkey;

ALTER TABLE startups ADD CONSTRAINT startups_owner_id_fkey
    FOREIGN KEY (owner_id)
    REFERENCES users
    ON DELETE CASCADE
    ON UPDATE NO ACTION