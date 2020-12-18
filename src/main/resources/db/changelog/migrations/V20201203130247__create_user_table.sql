CREATE TABLE users
(
    user_id      BIGSERIAL PRIMARY KEY,
    role_id      SMALLINT,
    name         VARCHAR(80) NOT NULL,
    email        VARCHAR(80) NOT NULL UNIQUE,
    password     VARCHAR     NOT NULL,
    phone        VARCHAR(40),
    is_blocked   BOOLEAN   DEFAULT FALSE,
    block_reason TEXT,
    blocked_at   TIMESTAMP,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id)
        REFERENCES roles (role_id)
);

INSERT INTO users (user_id, role_id, name, email, password)
VALUES (1, 1, 'Администратор Александр', 'mngful5@mail.ru',
        '$2y$12$Y2k6ZkxWVlTYU3lnjiTz5.w5piAKxxMsb5uwlsJjga23TmXzxRu5u');

SELECT setval('users_user_id_seq', 1, true);