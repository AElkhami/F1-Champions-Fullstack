CREATE USER f1user WITH ENCRYPTED PASSWORD 'f1password';
GRANT ALL PRIVILEGES ON DATABASE f1db TO f1user;

-- Schema permissions
GRANT USAGE ON SCHEMA public TO f1user;
GRANT CREATE ON SCHEMA public TO f1user;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON TABLES TO f1user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL ON SEQUENCES TO f1user;

-- Pre-created tables
CREATE TABLE champion_entity (
    id UUID PRIMARY KEY,
    season VARCHAR(10) NOT NULL,
    driver_id VARCHAR(255) NOT NULL,
    driver_name VARCHAR(255) NOT NULL,
    constructor VARCHAR(255) NOT NULL
);
ALTER TABLE champion_entity OWNER TO f1user;

CREATE TABLE season_details_entity (
    id UUID PRIMARY KEY,
    season VARCHAR(255) NOT NULL,
    round VARCHAR(255) NOT NULL,
    race_name VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    winner_id VARCHAR(255) NOT NULL,
    winner_name VARCHAR(255) NOT NULL,
    constructor VARCHAR(255) NOT NULL
);
ALTER TABLE season_details_entity OWNER TO f1user;
