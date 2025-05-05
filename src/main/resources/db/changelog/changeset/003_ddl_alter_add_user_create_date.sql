--liquibase formatted sql
--changeset levin:add_user_create_date

ALTER TABLE users ADD COLUMN create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT now();

--rollback ALTER TABLE users DROP COLUMN create_date;
