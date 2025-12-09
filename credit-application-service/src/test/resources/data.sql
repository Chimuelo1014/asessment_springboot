-- Test data for H2 database

-- Insert test users (passwords are BCrypt encoded 'password')
INSERT INTO users (id, username, password, email, role) VALUES
(1, 'admin', '$2a$10$N9qN.VLz68e8vLMl5R5YvejLXg7D5YaVLlC5SmMU0rDMEj4VPE9.C', 'admin@test.com', 'ROLE_ADMIN'),
(2, 'analyst', '$2a$10$N9qN.VLz68e8vLMl5R5YvejLXg7D5YaVLlC5SmMU0rDMEj4VPE9.C', 'analyst@test.com', 'ROLE_ANALISTA'),
(3, 'affiliate', '$2a$10$N9qN.VLz68e8vLMl5R5YvejLXg7D5YaVLlC5SmMU0rDMEj4VPE9.C', 'affiliate@test.com', 'ROLE_AFILIADO');

-- Insert test affiliates
INSERT INTO affiliates (id, document, full_name, email, phone, monthly_salary, status, affiliation_date) VALUES
(1, '1017654311', 'Juan Perez', 'juan.perez@test.com', '3001234567', 3000000.00, 'ACTIVE', '2023-01-15'),
(2, '1234567890', 'Maria Lopez', 'maria.lopez@test.com', '3007654321', 4000000.00, 'ACTIVE', '2023-06-01'),
(3, '9876543210', 'Carlos Garcia', 'carlos.garcia@test.com', '3009876543', 2500000.00, 'INACTIVE', '2024-01-01');

