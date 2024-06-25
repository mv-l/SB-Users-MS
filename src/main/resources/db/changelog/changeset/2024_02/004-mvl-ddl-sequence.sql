-- liquibase formatted sql
-- changeset author:mvl, dbms:PostgreSQL

-- Одна последовательность для всех таблиц
DROP SEQUENCE IF EXISTS users_sequence;
CREATE SEQUENCE users_sequence START WITH 1 INCREMENT BY 10;
