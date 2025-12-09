-- Schema for H2 test database (compatible with PostgreSQL mode)

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Affiliates table
CREATE TABLE IF NOT EXISTS affiliates (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    monthly_salary DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    affiliation_date DATE NOT NULL,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Credit applications table
CREATE TABLE IF NOT EXISTS credit_applications (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    requested_amount DECIMAL(19,2) NOT NULL,
    term_months INTEGER NOT NULL,
    interest_rate DECIMAL(5,2),
    status VARCHAR(20) NOT NULL,
    application_date TIMESTAMP NOT NULL,
    evaluation_date TIMESTAMP,
    analyst_comments TEXT,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (affiliate_id) REFERENCES affiliates(id)
);

-- Risk evaluations table
CREATE TABLE IF NOT EXISTS risk_evaluations (
    id BIGSERIAL PRIMARY KEY,
    credit_application_id BIGINT UNIQUE NOT NULL,
    score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    recommendation VARCHAR(50),
    evaluation_message TEXT,
    evaluation_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (credit_application_id) REFERENCES credit_applications(id)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_affiliates_document ON affiliates(document);
CREATE INDEX IF NOT EXISTS idx_credit_applications_affiliate ON credit_applications(affiliate_id);
CREATE INDEX IF NOT EXISTS idx_credit_applications_status ON credit_applications(status);
