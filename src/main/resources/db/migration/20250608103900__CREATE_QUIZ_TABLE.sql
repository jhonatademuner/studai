CREATE TABLE tquiz (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(512) NOT NULL,
    language_code VARCHAR(4) NOT NULL,
    source_type VARCHAR(50) NOT NULL,
    source_content TEXT NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tuser(id)
);
