CREATE TABLE IF NOT EXISTS task (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created BIGINT,
    updated BIGINT,
    status VARCHAR(50),
    due_date BIGINT
);