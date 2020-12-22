CREATE TABLE tasks
(
    report_id      BIGSERIAL PRIMARY KEY,
    tracking_id    BIGINT   NOT NULL,
    startup_id     BIGINT   NOT NULL,
    owner_id       BIGINT   NOT NULL,
    task_status_id INT      NOT NULL,
    name           VARCHAR  NOT NULL,
    description    TEXT,
    deadline_at    TIMESTAMP,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP,
    FOREIGN KEY (tracking_id) REFERENCES trackings (tracking_id) ON DELETE CASCADE,
    FOREIGN KEY (startup_id) REFERENCES startups (startup_id) ON DELETE CASCADE,
    FOREIGN KEY (task_status_id) REFERENCES task_statuses (task_status_id),
    FOREIGN KEY (owner_id) REFERENCES users (user_id)
)
