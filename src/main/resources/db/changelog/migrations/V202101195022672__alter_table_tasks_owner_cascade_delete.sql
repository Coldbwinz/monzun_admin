ALTER TABLE tasks DROP CONSTRAINT tasks_owner_id_fkey;

ALTER TABLE tasks ADD CONSTRAINT tasks_owner_id_fkey
    FOREIGN KEY (owner_id)
    REFERENCES users
    ON DELETE CASCADE
    ON UPDATE NO ACTION