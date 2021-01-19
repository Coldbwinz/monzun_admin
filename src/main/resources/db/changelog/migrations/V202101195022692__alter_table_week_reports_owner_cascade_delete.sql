ALTER TABLE week_reports DROP CONSTRAINT week_reports_owner_id_fkey;

ALTER TABLE week_reports ADD CONSTRAINT week_reports_owner_id_fkey
    FOREIGN KEY (owner_id)
    REFERENCES users
    ON DELETE CASCADE
    ON UPDATE NO ACTION