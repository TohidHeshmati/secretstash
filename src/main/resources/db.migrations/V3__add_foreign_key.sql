-- Add foreign key constraint between notes and users tables
ALTER TABLE notes
ADD FOREIGN KEY (user_id) REFERENCES users(id);