CREATE TABLE roles
(
    role_id SMALLSERIAL PRIMARY KEY,
    name    VARCHAR(40) NOT NULL,
    title   VARCHAR(40) NOT NULL
);

INSERT INTO roles
VALUES (1, 'admin', 'Администратор'),
       (2, 'startup', 'Участник'),
       (3, 'tracker', 'Наблюдатель');
