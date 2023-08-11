DROP TABLE IF EXISTS category CASCADE;
CREATE TABLE IF NOT EXISTS category (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(254) NOT NULL,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT UQ_USERS_EMAIL UNIQUE (email),
    CONSTRAINT UQ_USERS_NAME UNIQUE (name)
);

DROP TABLE IF EXISTS event CASCADE;
CREATE TABLE IF NOT EXISTS event (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation CHARACTER VARYING(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    description CHARACTER VARYING(7000) NOT NULL,
    event_date timestamp WITHOUT TIME ZONE NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    participant_limit BIGINT DEFAULT 0,
    request_moderation BOOLEAN DEFAULT TRUE,
    title CHARACTER VARYING(120) NOT NULL,
    status CHARACTER VARYING(20) NOT NULL,
    confirmed_requests BIGINT,
    created_on timestamp WITHOUT TIME ZONE NOT NULL,
    published_on timestamp WITHOUT TIME ZONE,
    initiator_id BIGINT NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    views BIGINT DEFAULT 0,
    CONSTRAINT fk_event_to_category FOREIGN KEY(category_id) REFERENCES category(id),
    CONSTRAINT fk_event_to_initiator FOREIGN KEY(initiator_id) REFERENCES users(id) --,
);

DROP TABLE IF EXISTS request CASCADE;
CREATE TABLE IF NOT EXISTS request (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created timestamp WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT  NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT UQ_REQUESTS UNIQUE (event_id, requester_id),
    CONSTRAINT fk_requests_to_event FOREIGN KEY(event_id) REFERENCES event(id),
    CONSTRAINT fk_requests_to_user FOREIGN KEY(requester_id) REFERENCES users(id)
);

DROP TABLE IF EXISTS compilation CASCADE;
CREATE TABLE IF NOT EXISTS compilation (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS compilation_event CASCADE;
CREATE TABLE IF NOT EXISTS compilation_event (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    UNIQUE (compilation_id, event_id),
    CONSTRAINT fk_ce_to_compilation FOREIGN KEY(compilation_id) REFERENCES compilation(id),
    CONSTRAINT fk_ce_to_event FOREIGN KEY(event_id) REFERENCES event(id)
);