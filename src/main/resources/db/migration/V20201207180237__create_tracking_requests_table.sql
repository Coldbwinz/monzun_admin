CREATE TABLE tracking_requests
(
    id          BIGSERIAL PRIMARY KEY,
    startup_id  INT NOT NULL,
    tracking_id INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (startup_id) REFERENCES startups (startup_id),
    FOREIGN KEY (tracking_id) REFERENCES trackings (tracking_id)
)