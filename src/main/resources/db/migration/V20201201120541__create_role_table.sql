CREATE TABLE roles (
    role_id smallint primary key,
    name varchar(40) NOT NULL,
    title varchar(40) NOT NULL
);

INSERT INTO roles VALUES
    (1, 'admin', 'Администратор'),
    (2, 'startup', 'Участник'),
    (3, 'tracker', 'Наблюдатель');
