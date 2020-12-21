CREATE TABLE week_reports
(
    report_id   BIGSERIAL PRIMARY KEY,
    tracking_id BIGINT   NOT NULL,
    startup_id  BIGINT   NOT NULL,
    owner_id    BIGINT   NOT NULL,
    week        INT      NOT NULL,
    estimate    SMALLINT NOT NULL,
    comment     TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (tracking_id) REFERENCES trackings (tracking_id) ON DELETE CASCADE,
    FOREIGN KEY (startup_id) REFERENCES startups (startup_id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (user_id)
)
