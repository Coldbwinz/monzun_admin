CREATE TABLE attachments
(
    attachment_id     BIGSERIAL PRIMARY KEY,
    uuid              UUID,
    url               TEXT,
    polytable_id      BIGINT,
    polytable_type    VARCHAR(20),
    owner_id          BIGINT,
    filename          TEXT,
    original_filename TEXT,
    path              VARCHAR,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users (user_id)
)