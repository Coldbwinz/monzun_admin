CREATE TABLE startup_trackings
(
    id          BIGSERIAL PRIMARY KEY,
    startup_id  INT NOT NULL,
    tracking_id INT NOT NULL,
    tracker_id  BIGINT NOT NULL,
    FOREIGN KEY (startup_id) REFERENCES startups (startup_id),
    FOREIGN KEY (tracking_id) REFERENCES trackings (tracking_id),
    FOREIGN KEY (tracker_id) REFERENCES users (user_id)
)