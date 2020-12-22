CREATE TABLE task_statuses
(
    task_status_id SERIAL PRIMARY KEY,
    name           VARCHAR NOT NULL,
    alias          VARCHAR NOT NULL
);

INSERT INTO task_statuses
VALUES (1, 'to do', 'Планируется'),
       (2, 'doing', 'В работе'),
       (3, 'in check', 'В проверке'),
       (4, 'done', 'Готово');