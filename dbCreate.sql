DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS base_user CASCADE;
DROP TABLE IF EXISTS client_details CASCADE;
DROP TABLE IF EXISTS executive_details CASCADE;
DROP TABLE IF EXISTS loan_type CASCADE;
DROP TABLE IF EXISTS mortgage_loan CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS executive CASCADE;
DROP TABLE IF EXISTS document_type CASCADE;
DROP TABLE IF EXISTS document CASCADE;
DROP TABLE IF EXISTS mortgage_document CASCADE;
DROP TABLE IF EXISTS mortgage_loan_review CASCADE;
DROP TABLE IF EXISTS preapproved_mortgage_loan CASCADE;
DROP TABLE IF EXISTS loan_type_required_document CASCADE;
DROP TABLE IF EXISTS loan_status CASCADE;
DROP TABLE IF EXISTS mortgage_loan_pending_documentation CASCADE;
DROP TABLE IF EXISTS pending_documentation CASCADE;


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
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    max_term INTEGER NOT NULL,
    min_interest_rate FLOAT NOT NULL,
    max_interest_rate FLOAT NOT NULL,
    max_financed_percentage FLOAT NOT NULL
);

INSERT INTO loan_type (id, name, max_term, min_interest_rate, max_interest_rate, max_financed_percentage)
VALUES (1, 'Primera vivienda', 30, 0.035, 0.05, 0.8),
       (2, 'Segunda vivienda', 20, 0.04, 0.06, 0.7),
       (3, 'Propiedades comerciales', 25, 0.05, 0.07, 0.6),
       (4, 'Remodelación', 15, 0.045, 0.06, 0.5);

CREATE TABLE mortgage_loan (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id INTEGER NOT NULL,
    loan_type_id INTEGER NOT NULL,
    payment_term INTEGER NOT NULL,
    financed_amount INTEGER NOT NULL,
    interest_rate FLOAT NOT NULL,
    status_id TEXT NOT NULL DEFAULT 'E1'
);


CREATE TABLE loan_status (
    id TEXT PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL
);

INSERT INTO loan_status (id, name, description)
    VALUES ('E1', 'En Revisión Inicial', 'La solicitud ha sido recibida y está en proceso de verificación preliminar.'),
        ('E2', 'Pendiente de Documentación', 'La solicitud está en espera porque falta uno o más documentos importantes o se requiere información adicional del cliente.'),
        ('E3', 'En Evaluación', 'La solicitud ha pasado la revisión inicial y está siendo evaluada por un ejecutivo.'),
        ('E4', 'Pre-Aprobada', 'La solicitud ha sido evaluada y cumple con los criterios básicos del banco, por lo que ha sido pre-aprobada'),
        ('E5', 'En Aprobación Final', 'El cliente ha aceptado las condiciones propuestas, y la solicitud se encuentra en proceso de aprobación final.'),
        ('E6', 'Aprobada', 'La solicitud ha sido aprobada y está lista para el desembolso.'),
        ('E7', 'Rechazada', 'La solicitud ha sido evaluada y, tras el análisis, no cumple con los criterios establecidos por el banco.'),
        ('E8', 'Cancelada por el Cliente', 'El cliente ha decidido cancelar la solicitud antes de que esta sea aprobada.'),
        ('E9', 'En Desembolso', 'La solicitud ha sido aprobada y se está ejecutando el proceso de desembolso del monto aprobado');

CREATE TABLE document_type (
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT
);

INSERT INTO document_type
    VALUES (1, 'Comprobante de Ingresos'),
           (2, 'Certificado de Avalúo'),
           (3, 'Historial crediticio'),
           (4, 'Escritura de la primera vivienda'),
           (5, 'Estado financiero del negocio'),
           (6, 'Plan de negocios'),
           (7, 'Presupuesto de remodelación'),
           (8, 'Certificado de avalúo actualizado');

CREATE TABLE document (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    data bytea NOT NULL
--     document_type_id BIGINT NOT NULL,
--     FOREIGN KEY (document_type_id) REFERENCES document_type(id)
);

CREATE TABLE loan_type_required_document (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    loan_type_id BIGINT NOT NULL,
    document_type_id BIGINT NOT NULL,
    FOREIGN KEY (loan_type_id) REFERENCES loan_type(id),
    FOREIGN KEY (document_type_id) REFERENCES document_type(id)
);

INSERT INTO loan_type_required_document (loan_type_id, document_type_id)
    VALUES (1, 1),
           (1,2),
           (1, 3),
           (2, 1),
           (2, 2),
           (2, 4),
           (2, 3),
           (3, 5),
           (3, 5),
           (3, 2),
           (3, 6),
           (4, 1),
           (3, 7),
           (3, 8);


CREATE TABLE mortgage_document (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mortgage_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL,
    FOREIGN KEY (mortgage_id) REFERENCES mortgage_loan(id),
    FOREIGN KEY (document_id) REFERENCES document(id)
);

CREATE TABLE mortgage_loan_review (
    id BIGINT PRIMARY KEY REFERENCES mortgage_loan(id),
    review_requester_id BIGINT NOT NULL,
    has_been_reviewed BOOLEAN NOT NULL DEFAULT FALSE,
    has_been_approved BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (review_requester_id) REFERENCES executive(id)
);

CREATE TABLE preapproved_mortgage_loan (
    id BIGINT PRIMARY KEY REFERENCES mortgage_loan(id),
    administration_fee BIGINT NOT NULL,
    credit_insurance_fee BIGINT NOT NULL,
    fire_insurance_fee BIGINT NOT NULL,
    total_monthly_cost BIGINT NOT NULL,
    total_cost BIGINT NOT NULL
);


CREATE TABLE pending_documentation (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    mortgage_id BIGINT NOT NULL,
    document_type_id BIGINT NOT NULL,
    FOREIGN KEY (mortgage_id) REFERENCES mortgage_loan(id),
    FOREIGN KEY (document_type_id) REFERENCES document_type(id),
    UNIQUE(mortgage_id, document_type_id)
);


