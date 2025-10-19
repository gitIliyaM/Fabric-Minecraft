CREATE DATABASE minecraft;
CREATE USER minecraft WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE minecraft TO minecraft;

\c messagemod;

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,
                          uuid UUID NOT NULL,
                          text VARCHAR(256) NOT NULL
);

GRANT ALL PRIVILEGES ON TABLE messages TO minecraft;
GRANT USAGE, SELECT ON SEQUENCE messages_id_seq TO minecraft;