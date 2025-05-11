--liquibase formatted sql

--changeset Maksim Levin:create_calc_events_table
CREATE TABLE IF NOT EXISTS calc_events (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id),
    first NUMERIC NOT NULL,
    second NUMERIC NOT NULL,
    result NUMERIC NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type VARCHAR(50) NOT NULL
);

--rollback DROP TABLE calc_events; 