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
    report_data bigint  NOT NULL
);

CREATE TABLE report_cat
(
    id_report   bigserial PRIMARY KEY,
    caption     TEXT   NOT NULL,
    report_date DATE   NOT NULL,
    file_path   TEXT   NOT NULL,
    file_size   bigint NOT NULL,
    report_data bigint  NOT NULL
);

-- changeSet ivan:2

CREATE TABLE volunteer
(
    id_volunteer bigint PRIMARY KEY,
    user_name TEXT NOT NULL,
    name_volunteer TEXT,
    status_volunteer BOOLEAN DEFAULT true
);