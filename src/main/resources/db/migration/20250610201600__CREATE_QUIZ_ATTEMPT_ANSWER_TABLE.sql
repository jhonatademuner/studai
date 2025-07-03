CREATE TABLE tquiz_attempt_answer (
    id UUID PRIMARY KEY,
    attempt_id UUID NOT NULL,
    question_id UUID NOT NULL,
    answer TEXT NOT NULL,
    correct BOOLEAN NOT NULL,
    FOREIGN KEY (attempt_id) REFERENCES tquiz_attempt(id),
    FOREIGN KEY (question_id) REFERENCES tquiz_question(id)
);