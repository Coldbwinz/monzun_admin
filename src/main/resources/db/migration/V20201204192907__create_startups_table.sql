CREATE TABLE startups
(
    startup_id    SERIAL PRIMARY KEY,
    logo_id       BIGINT,
    owner_id      BIGINT      NOT NULL,
    name          VARCHAR(80) NOT NULL,
    description   TEXT,
    business_plan TEXT,
    tasks         TEXT,
    growth_plan   TEXT,
    use_area      TEXT,
    points        TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP,
    FOREIGN KEY (logo_id) REFERENCES attachments (attachment_id),
    FOREIGN KEY (owner_id) REFERENCES users (user_id)
)