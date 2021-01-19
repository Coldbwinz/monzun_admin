ALTER TABLE startup_trackings DROP CONSTRAINT startup_trackings_startup_id_fkey;
ALTER TABLE startup_trackings DROP CONSTRAINT startup_trackings_tracking_id_fkey;

ALTER TABLE startup_trackings ADD CONSTRAINT startup_trackings_startup_id_fkey
    FOREIGN KEY (startup_id)
    REFERENCES startups
    ON DELETE CASCADE
    ON UPDATE NO ACTION;

ALTER TABLE startup_trackings ADD CONSTRAINT startup_trackings_tracking_id_fkey
    FOREIGN KEY (tracking_id)
        REFERENCES trackings
        ON DELETE CASCADE
        ON UPDATE NO ACTION