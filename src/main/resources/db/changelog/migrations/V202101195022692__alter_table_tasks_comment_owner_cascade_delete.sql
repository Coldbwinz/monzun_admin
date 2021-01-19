ALTER TABLE task_comments DROP CONSTRAINT task_comments_owner_id_fkey;

ALTER TABLE task_comments ADD CONSTRAINT task_comments_owner_id_fkey
    FOREIGN KEY (owner_id)
    REFERENCES users
    ON DELETE CASCADE
    ON UPDATE NO ACTION