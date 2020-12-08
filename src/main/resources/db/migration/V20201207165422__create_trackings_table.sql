CREATE TABLE trackings
(
    tracking_id SERIAL PRIMARY KEY,
    logo_id     BIGINT,
    name        VARCHAR(120) NOT NULL UNIQUE,
    description TEXT,
    is_active   boolean   DEFAULT TRUE,
    started_at  TIMESTAMP,
    ended_at    TIMESTAMP    NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    FOREIGN KEY (logo_id) REFERENCES attachments (attachment_id)
);