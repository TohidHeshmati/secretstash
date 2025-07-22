-- Create the users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create the notes table
CREATE TABLE notes (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    user_id BIGINT NOT NULL
);

-- Add foreign key constraint between notes and users tables
ALTER TABLE notes
ADD FOREIGN KEY (user_id) REFERENCES users(id);