ALTER TABLE startup_trackings DROP CONSTRAINT startup_trackings_tracker_id_fkey;

ALTER TABLE startup_trackings ADD CONSTRAINT startup_trackings_tracker_id_fkey
    FOREIGN KEY (tracker_id)
    REFERENCES users
    ON DELETE SET NULL
    ON UPDATE NO ACTION