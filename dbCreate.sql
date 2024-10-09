DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS base_user CASCADE;
DROP TABLE IF EXISTS client_details CASCADE;
DROP TABLE IF EXISTS executive_details CASCADE;
DROP TABLE IF EXISTS loan_type CASCADE;
DROP TABLE IF EXISTS mortgage_loan CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS executive CASCADE;



CREATE TABLE role (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL
);

INSERT INTO role (id, name)
    VALUES
        (1, 'CLIENT'),
        (2, 'EXECUTIVE'),
        (3, 'ADMIN');

CREATE TABLE base_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE(email),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE client (
    id BIGINT PRIMARY KEY REFERENCES base_user(id),
    name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    birth_date DATE NOT NULL,
    gender TEXT NOT NULL,
    nationality TEXT NOT NULL,
    address TEXT NOT NULL,
    phone_number TEXT NOT NULL
);

CREATE TABLE executive (
    id BIGINT PRIMARY KEY REFERENCES base_user(id),
    name TEXT NOT NULL
);

CREATE TABLE loan_type (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    max_term INTEGER NOT NULL,
    min_interest_rate FLOAT NOT NULL,
    max_interest_rate FLOAT NOT NULL,
    max_financed_percentage FLOAT NOT NULL,
    required_documents TEXT[]
);

CREATE TABLE mortgage_loan (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id INTEGER NOT NULL,
    loan_type_id INTEGER NOT NULL,
    payment_term INTEGER NOT NULL,
    financed_amount INTEGER NOT NULL,
    interest_rate FLOAT NOT NULL,
    documents bytea[] NOT NULL,
    status INTEGER NOT NULL DEFAULT 0
);

