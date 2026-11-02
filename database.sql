CREATE DATABASE IF NOT EXISTS document_approval_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE document_approval_db;

CREATE TABLE IF NOT EXISTS authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100),
    position VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS reviewers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100),
    position VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    document_type VARCHAR(100),
    file_name VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT NOT NULL,
    CONSTRAINT fk_documents_author
        FOREIGN KEY (author_id) REFERENCES authors(id)
);

CREATE TABLE IF NOT EXISTS approval_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    status ENUM(
        'DRAFT',
        'SUBMITTED',
        'UNDER_REVIEW',
        'APPROVED'
    ) NOT NULL DEFAULT 'DRAFT',
    submitted_at DATETIME NULL,
    reviewed_at DATETIME NULL,
    f1 DECIMAL(5,2) DEFAULT 0,
    f2 DECIMAL(5,2) DEFAULT 0,
    f3 DECIMAL(5,2) DEFAULT 0,
    integral_index DECIMAL(5,2) DEFAULT 0,

    CONSTRAINT fk_requests_document
        FOREIGN KEY (document_id) REFERENCES documents(id),

    CONSTRAINT fk_requests_reviewer
        FOREIGN KEY (reviewer_id) REFERENCES reviewers(id)
);

CREATE INDEX idx_documents_author
ON documents(author_id);

CREATE INDEX idx_requests_document
ON approval_requests(document_id);

CREATE INDEX idx_requests_reviewer
ON approval_requests(reviewer_id);

CREATE INDEX idx_requests_status
ON approval_requests(status);

CREATE INDEX idx_requests_integral_index
ON approval_requests(integral_index);
