--liquibase formatted sql
--changeset levin:add_columns_to_users
ALTER TABLE users
    ADD COLUMN first_arg INT,
    ADD COLUMN second_arg INT,
    ADD COLUMN result INT;