CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL
);

CREATE TABLE document_types
(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(500) NOT NULL
);

CREATE TABLE ticket_categories
(
    id       SERIAL PRIMARY KEY,
    category VARCHAR(500) NOT NULL
);

CREATE TABLE code_operation_direction
(
    id        SERIAL PRIMARY KEY,
    direction VARCHAR(500) NOT NULL
);

CREATE TABLE operation_code_names
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL
);

CREATE TABLE method_list
(
    id          SERIAL PRIMARY KEY,
    date        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    method_name VARCHAR(500),
    method_ver  INT CHECK (method_list.method_ver >= 0)
);