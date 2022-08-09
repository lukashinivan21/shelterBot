-- liquibase formatted sql

-- changeSet ivan:1

CREATE TABLE candidate_dog_shelter
(
    id_candidate       bigint PRIMARY KEY,
    name_candidate     TEXT,
    username_candidate TEXT,
    phone_number       TEXT UNIQUE,
    bot_state          TEXT
);

CREATE TABLE candidate_cat_shelter
(
    id_candidate       bigint PRIMARY KEY,
    name_candidate     TEXT,
    username_candidate TEXT,
    phone_number       TEXT UNIQUE,
    bot_state          TEXT
);

CREATE TABLE report_dog
(
    id_report   bigserial PRIMARY KEY,
    caption     TEXT   NOT NULL,
    report_date DATE   NOT NULL,
    file_path   TEXT   NOT NULL,
    file_size   bigint NOT NULL,
    report_data oid    NOT NULL
);

CREATE TABLE report_cat
(
    id_report   bigserial PRIMARY KEY,
    caption     TEXT   NOT NULL,
    report_date DATE   NOT NULL,
    file_path   TEXT   NOT NULL,
    file_size   bigint NOT NULL,
    report_data oid    NOT NULL
);