CREATE TABLE password_reset_tokens
(
    token_id   BIGSERIAL PRIMARY KEY,
    token      VARCHAR   NOT NULL,
    user_id    BIGINT    NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
)