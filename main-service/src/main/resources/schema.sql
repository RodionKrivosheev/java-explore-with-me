DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;

CREATE TABLE IF NOT EXISTS categories (
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE                      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254) UNIQUE                     NOT NULL,
    name  varchar(250)                            NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations (
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL                                    NOT NULL,
    lon REAL                                    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (lat, lon)
);

create table IF NOT EXISTS events (
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT REFERENCES categories (id)       NOT NULL,
    created_on         TIMESTAMP                               NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP                               NOT NULL,
    initiator_id       BIGINT REFERENCES users (ID)            NOT NULL,
    location_id        BIGINT REFERENCES locations (ID)        NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INTEGER                                 NOT NULL,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR(10)                            NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP                               NOT NULL,
    event_id     BIGINT REFERENCES events (id),
    requester_id BIGINT REFERENCES users (id),
    status       VARCHAR(255)                            NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(50) UNIQUE                      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    event_id       BIGINT REFERENCES events (id),
    compilation_id BIGINT REFERENCES compilations (id),
    PRIMARY KEY (event_id, compilation_id)
);