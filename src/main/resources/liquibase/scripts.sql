-- liquibase formatted sql

-- changeSet ivan:1

CREATE TABLE candidate
(
    id_candidate       bigint PRIMARY KEY,
    name_candidate     TEXT,
    username_candidate TEXT,
    phone_number       TEXT UNIQUE,
    bot_state          TEXT
);

-- changeSet ivan:2

CREATE TABLE dog_candidate
(
    id_candidate       bigint PRIMARY KEY,
    name_candidate     TEXT,
    username_candidate TEXT,
    phone_number       TEXT UNIQUE,
    bot_state          TEXT
);

CREATE TABLE cat_candidate
(
    id_candidate       bigint PRIMARY KEY,
    name_candidate     TEXT,
    username_candidate TEXT,
    phone_number       TEXT UNIQUE,
    bot_state          TEXT
);