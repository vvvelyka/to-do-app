CREATE TABLE IF NOT EXISTS "user"
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_users_username ON "user" (username);