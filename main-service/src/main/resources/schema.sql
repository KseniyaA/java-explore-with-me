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
    created_on timestamp WITHOUT TIME ZONE NOT NULL,
    published_on timestamp WITHOUT TIME ZONE,
    initiator_id BIGINT NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    location_id BIGINT,
    CONSTRAINT fk_event_to_category FOREIGN KEY(category_id) REFERENCES category(id),
    CONSTRAINT fk_event_to_initiator FOREIGN KEY(initiator_id) REFERENCES users(id),
    CONSTRAINT fk_event_to_location FOREIGN KEY(location_id) REFERENCES location(id)
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

DROP TABLE IF EXISTS location CASCADE;
CREATE TABLE IF NOT EXISTS location (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    radius DOUBLE PRECISION NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT UQ_LOCATION UNIQUE (name),
    CONSTRAINT UQ_LOT_LON UNIQUE (lat, lon)
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
declare
    dist float = 0;
    rad_lat1 float;
    rad_lat2 float;
    theta float;
    rad_theta float;
BEGIN
    IF lat1 = lat2 AND lon1 = lon2
    THEN
        RETURN dist;
    ELSE
        -- переводим градусы широты в радианы
        rad_lat1 = pi() * lat1 / 180;
        -- переводим градусы долготы в радианы
        rad_lat2 = pi() * lat2 / 180;
        -- находим разность долгот
        theta = lon1 - lon2;
        -- переводим градусы в радианы
        rad_theta = pi() * theta / 180;
        -- находим длину ортодромии
        dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

        IF dist > 1
            THEN dist = 1;
        END IF;

        dist = acos(dist);
        -- переводим радианы в градусы
        dist = dist * 180 / pi();
        -- переводим градусы в километры
        dist = dist * 60 * 1.8524;

        RETURN dist;
    END IF;
END;
'
LANGUAGE PLPGSQL;


