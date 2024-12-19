CREATE SCHEMA IF NOT EXISTS todo;

CREATE TABLE IF NOT EXISTS todo
(
    id              BIGSERIAL PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    check_mark      BOOLEAN      NOT NULL DEFAULT FALSE,
    completion_date TIMESTAMP,
    due_date        DATE,
    creation_date   TIMESTAMP
);