-- =====================================================
-- КУРСОВА РОБОТА
-- Інформаційна система погодження внутрішніх документів
-- Варіант №25
-- =====================================================


-- 1. СТВОРЕННЯ БАЗИ ДАНИХ

CREATE DATABASE IF NOT EXISTS document_approval_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE document_approval_db;


-- =====================================================
-- 2. ТАБЛИЦЯ AUTHORS (АВТОРИ)
-- =====================================================

CREATE TABLE IF NOT EXISTS authors (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100),
    position VARCHAR(100)
    );


-- =====================================================
-- 3. ТАБЛИЦЯ REVIEWERS (РЕЦЕНЗЕНТИ)
-- =====================================================

CREATE TABLE IF NOT EXISTS reviewers (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100),
    position VARCHAR(100)
    );


-- =====================================================
-- 4. ТАБЛИЦЯ DOCUMENTS (ДОКУМЕНТИ)
-- =====================================================

CREATE TABLE IF NOT EXISTS documents (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         title VARCHAR(200) NOT NULL,
    description TEXT,
    document_type VARCHAR(100),
    file_name VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    author_id BIGINT NOT NULL,

    CONSTRAINT fk_documents_author
    FOREIGN KEY (author_id)
    REFERENCES authors(id)
    );


-- =====================================================
-- 5. ТАБЛИЦЯ APPROVAL_REQUESTS (ЗАПИТИ НА ПОГОДЖЕННЯ)
-- =====================================================

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
    FOREIGN KEY (document_id)
    REFERENCES documents(id),

    CONSTRAINT fk_requests_reviewer
    FOREIGN KEY (reviewer_id)
    REFERENCES reviewers(id)
    );


-- =====================================================
-- 6. СТВОРЕННЯ ІНДЕКСІВ
-- =====================================================

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


-- =====================================================
-- 7. ДОДАВАННЯ АВТОРІВ
-- =====================================================

INSERT INTO authors (full_name, email, department, position)
SELECT 'Іван Петренко', 'petrenko@example.com',
       'Фінансовий відділ', 'Головний спеціаліст'
    WHERE NOT EXISTS (
    SELECT 1 FROM authors
    WHERE email = 'petrenko@example.com'
);

INSERT INTO authors (full_name, email, department, position)
SELECT 'Олена Коваленко', 'kovalenko@example.com',
       'Відділ кадрів', 'Менеджер'
    WHERE NOT EXISTS (
    SELECT 1 FROM authors
    WHERE email = 'kovalenko@example.com'
);


-- =====================================================
-- 8. ДОДАВАННЯ РЕЦЕНЗЕНТІВ
-- =====================================================

INSERT INTO reviewers (full_name, email, department, position)
SELECT 'Андрій Мельник', 'melnyk@example.com',
       'Юридичний відділ', 'Юрисконсульт'
    WHERE NOT EXISTS (
    SELECT 1 FROM reviewers
    WHERE email = 'melnyk@example.com'
);

INSERT INTO reviewers (full_name, email, department, position)
SELECT 'Марія Шевченко', 'shevchenko@example.com',
       'Фінансовий відділ', 'Керівник відділу'
    WHERE NOT EXISTS (
    SELECT 1 FROM reviewers
    WHERE email = 'shevchenko@example.com'
);


-- =====================================================
-- 9. ДОДАВАННЯ ДОКУМЕНТІВ
-- =====================================================

INSERT INTO documents
(title, description, document_type, file_name, author_id)
SELECT
    'Наказ про відпустки',
    'Документ щодо планування щорічних відпусток працівників.',
    'Наказ',
    'vacation_order.pdf',
    a.id
FROM authors a
WHERE a.email = 'petrenko@example.com'
  AND NOT EXISTS (
    SELECT 1 FROM documents
    WHERE title = 'Наказ про відпустки'
);

INSERT INTO documents
(title, description, document_type, file_name, author_id)
SELECT
    'Фінансовий звіт',
    'Звіт про фінансову діяльність підприємства.',
    'Звіт',
    'financial_report.pdf',
    a.id
FROM authors a
WHERE a.email = 'kovalenko@example.com'
  AND NOT EXISTS (
    SELECT 1 FROM documents
    WHERE title = 'Фінансовий звіт'
);


-- =====================================================
-- 10. ДОДАВАННЯ ЗАПИТІВ НА ПОГОДЖЕННЯ
-- =====================================================

INSERT INTO approval_requests
(document_id, reviewer_id, status,
 submitted_at, reviewed_at,
 f1, f2, f3, integral_index)

SELECT
    d.id,
    r.id,
    'DRAFT',
    NULL,
    NULL,
    80,
    60,
    40,
    63
FROM documents d
         JOIN reviewers r
              ON r.email = 'melnyk@example.com'
WHERE d.title = 'Наказ про відпустки'
  AND NOT EXISTS (
    SELECT 1 FROM approval_requests
    WHERE document_id = d.id
      AND reviewer_id = r.id
);


INSERT INTO approval_requests
(document_id, reviewer_id, status,
 submitted_at, reviewed_at,
 f1, f2, f3, integral_index)

SELECT
    d.id,
    r.id,
    'SUBMITTED',
    CURRENT_TIMESTAMP,
    NULL,
    90,
    70,
    50,
    73
FROM documents d
         JOIN reviewers r
              ON r.email = 'shevchenko@example.com'
WHERE d.title = 'Фінансовий звіт'
  AND NOT EXISTS (
    SELECT 1 FROM approval_requests
    WHERE document_id = d.id
      AND reviewer_id = r.id
);


-- =====================================================
-- 11. ПЕРЕВІРКА ТАБЛИЦЬ
-- =====================================================

SHOW TABLES;


-- =====================================================
-- 12. ПЕРЕВІРКА АВТОРІВ
-- =====================================================

SELECT * FROM authors;


-- =====================================================
-- 13. ПЕРЕВІРКА РЕЦЕНЗЕНТІВ
-- =====================================================

SELECT * FROM reviewers;


-- =====================================================
-- 14. ПЕРЕВІРКА ДОКУМЕНТІВ
-- =====================================================

SELECT * FROM documents;


-- =====================================================
-- 15. ПЕРЕВІРКА ЗАПИТІВ НА ПОГОДЖЕННЯ
-- =====================================================

SELECT * FROM approval_requests;


-- =====================================================
-- 16. ПЕРЕВІРКА ЗВ'ЯЗКІВ І ДАНИХ
-- =====================================================

SELECT
    ar.id AS request_id,
    d.title AS document_title,
    a.full_name AS author_name,
    r.full_name AS reviewer_name,
    ar.status,
    ar.f1,
    ar.f2,
    ar.f3,
    ar.integral_index
FROM approval_requests ar
         JOIN documents d
              ON ar.document_id = d.id
         JOIN authors a
              ON d.author_id = a.id
         JOIN reviewers r
              ON ar.reviewer_id = r.id;


-- =====================================================
-- КІНЕЦЬ SQL-СКРИПТУ
-- =====================================================