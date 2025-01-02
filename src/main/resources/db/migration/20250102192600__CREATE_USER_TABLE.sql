CREATE TABLE IF NOT EXISTS TUSER (
    ID UUID PRIMARY KEY,
    USERNAME VARCHAR(255) NOT NULL UNIQUE,
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL,
    ROLE VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
