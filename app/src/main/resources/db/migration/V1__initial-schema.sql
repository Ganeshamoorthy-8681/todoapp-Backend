CREATE TABLE IF NOT EXISTS users (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255),
   email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS task (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created BIGINT,
    updated BIGINT,
    status VARCHAR(50),
    due_date BIGINT,
    priority VARCHAR(50),
    category VARCHAR(50),
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id)
);