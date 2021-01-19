ALTER TABLE attachments DROP CONSTRAINT attachments_owner_id_fkey;

ALTER TABLE attachments ADD CONSTRAINT attachments_owner_id_fkey
    FOREIGN KEY (owner_id)
    REFERENCES users
    ON DELETE CASCADE
    ON UPDATE NO ACTION