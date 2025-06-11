CREATE TABLE tquiz_attempt (
    id UUID PRIMARY KEY,
    quiz_id UUID NOT NULL,
    user_id UUID,
    score NUMERIC(5, 2) NOT NULL,
    time_spent BIGINT NOT NULL,
    guest_user BOOLEAN NOT NULL,
    guest_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES tquiz(id),
    FOREIGN KEY (user_id) REFERENCES tuser(id)
);