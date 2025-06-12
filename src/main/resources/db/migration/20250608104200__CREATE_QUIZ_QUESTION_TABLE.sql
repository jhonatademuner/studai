CREATE TABLE tquiz_question (
    id UUID PRIMARY KEY,
    question_type VARCHAR(50) NOT NULL,
    statement TEXT NOT NULL,
    hint TEXT,
    explanation TEXT NOT NULL,
    correct_answer TEXT NOT NULL,
    options TEXT NOT NULL,
    quiz_id UUID NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES tquiz(id)
);