CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    monthly_salary DECIMAL(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    affiliation_date DATE NOT NULL,
    version BIGINT
);

CREATE TABLE credit_applications (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    requested_amount DECIMAL(19, 2) NOT NULL,
    term_months INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    application_date TIMESTAMP NOT NULL,
    evaluation_date TIMESTAMP,
    analyst_comments TEXT,
    version BIGINT
);

CREATE TABLE risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    credit_application_id BIGINT NOT NULL UNIQUE,
    score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    recommendation VARCHAR(50),
    evaluation_message TEXT,
    evaluation_date TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_affiliate_document ON affiliates(document);
CREATE INDEX idx_credit_app_affiliate ON credit_applications(affiliate_id);
CREATE INDEX idx_credit_app_status ON credit_applications(status);
