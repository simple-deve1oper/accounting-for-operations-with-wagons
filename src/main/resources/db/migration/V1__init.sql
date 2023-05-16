-- Типы поездов
CREATE TABLE types (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR (60) UNIQUE NOT NULL
);

--вагоны
CREATE TABLE wagons (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    number VARCHAR (7) UNIQUE NOT NULL,
    type_id BIGINT NOT NULL REFERENCES types (id),
    tare_weight REAL NOT NULL,
    load_capacity REAL NOT NULL
);

-- роли
CREATE TABLE roles (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	NAME VARCHAR UNIQUE NOT NULL
);

-- пользователи
CREATE TABLE people (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login VARCHAR(60) UNIQUE NOT NULL,
    password VARCHAR (255) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES roles(id)
);

-- станции
CREATE TABLE stations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR (100) NOT NULL
);

 -- пути
CREATE TABLE pathways (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    station_id BIGINT NOT NULL REFERENCES stations (id) ON DELETE CASCADE,
    number INT NOT NULL
);

-- грузы (справочник номенклатур грузов)
CREATE TABLE cargos (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR (100) NOT NULL
);

-- документы (натурный лист для приема вагонов)
CREATE TABLE documents (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    serial_number INT NOT NULL,
    wagon_number VARCHAR (7) NOT NULL,
    cargo_id BIGINT NOT NULL REFERENCES cargos (id),
    cargo_weight REAL NOT NULL,
    wagon_weight REAL NOT NULL,
    pathway_id BIGINT NOT NULL REFERENCES pathways (id),
    departure_date TIMESTAMP
);

INSERT INTO roles(name) VALUES ('GUEST'), ('MODERATOR'), ('ADMIN');

INSERT INTO people(login, password, role_id) VALUES ('tom', '1111', 3), ('alice', '1234', 2),
('bob', '5678', 1);

