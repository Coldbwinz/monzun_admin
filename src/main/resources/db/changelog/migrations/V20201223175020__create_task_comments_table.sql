CREATE TABLE task_comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    task_id    BIGINT    NOT NULL,
    owner_id   BIGINT    NOT NULL,
    text       TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks (task_id) ON DELETE CASCADE,
    FOREIGN KEY (owner_id) REFERENCES users (user_id) ON DELETE CASCADE
)