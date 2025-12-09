-- Passwords are 'password' encrypted with BCrypt
-- $2a$10$DxW7q/pX8pT6/pX8pT6/pX8pT6/pX8pT6/pX8pT6/pX8pT6/pX8pT6 (Fake, using a generic one found online for 'password')
-- Actual hash for 'password': $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7qa8q.F2.

INSERT INTO users (username, password, email, role, enabled, created_at)
VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7qa8q.F2.', 'admin@coopcredit.com', 'ROLE_ADMIN', TRUE, CURRENT_TIMESTAMP),
('analyst', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7qa8q.F2.', 'analyst@coopcredit.com', 'ROLE_ANALISTA', TRUE, CURRENT_TIMESTAMP);
