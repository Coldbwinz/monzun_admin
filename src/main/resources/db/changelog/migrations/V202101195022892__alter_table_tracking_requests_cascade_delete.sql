ALTER TABLE tracking_requests DROP CONSTRAINT tracking_requests_startup_id_fkey;
ALTER TABLE tracking_requests DROP CONSTRAINT tracking_requests_tracking_id_fkey;

ALTER TABLE tracking_requests ADD CONSTRAINT tracking_requests_startup_id_fkey
    FOREIGN KEY (startup_id)
    REFERENCES startups
    ON DELETE CASCADE
    ON UPDATE NO ACTION;

ALTER TABLE tracking_requests ADD CONSTRAINT tracking_requests_tracking_id_fkey
    FOREIGN KEY (tracking_id)
        REFERENCES trackings
        ON DELETE CASCADE
        ON UPDATE NO ACTION