CREATE TABLE IF NOT EXISTS endpoint_hit (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(100) NOT NULL,
    uri VARCHAR(100) NOT NULL,
    ip VARCHAR(28) NOT NULL,
    created timestamp WITHOUT TIME ZONE NOT NULL
);
