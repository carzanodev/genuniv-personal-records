CREATE TABLE IF NOT EXISTS student
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR NOT NULL,
    middle_name VARCHAR,
    last_name   VARCHAR NOT NULL,
    birthdate   DATE,
    address     VARCHAR,
    degree_id   INTEGER,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS faculty
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR NOT NULL,
    middle_name VARCHAR,
    last_name   VARCHAR NOT NULL,
    birthdate   DATE,
    address     VARCHAR,
    college_id  INTEGER,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS staff
(
    id          SERIAL PRIMARY KEY,
    first_name  VARCHAR NOT NULL,
    middle_name VARCHAR,
    last_name   VARCHAR NOT NULL,
    birthdate   DATE,
    address     VARCHAR,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    inserted_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION updated_at_now()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON student
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON faculty
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();
CREATE TRIGGER auto_updated_at
    BEFORE UPDATE
    ON staff
    FOR EACH ROW
EXECUTE PROCEDURE updated_at_now();