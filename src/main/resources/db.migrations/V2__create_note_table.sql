-- Create the note table
CREATE TABLE note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_note_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);