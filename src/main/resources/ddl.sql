CREATE TABLE instructors
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(10)  NOT NULL,
    introduction VARCHAR(100) NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   DATETIME(6)  NOT NULL,
    modified_at  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_instructors_status CHECK (status IN ('ACTIVE', 'DELETED'))
);

CREATE TABLE courses
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    instructor_id BIGINT       NOT NULL,
    title         VARCHAR(50)  NOT NULL,
    description   VARCHAR(200) NOT NULL,
    price         INT          NOT NULL,
    level         VARCHAR(20)  NOT NULL,
    status        VARCHAR(20)  NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    modified_at   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_courses_instructor FOREIGN KEY (instructor_id) REFERENCES instructors (id),
    CONSTRAINT chk_courses_price CHECK (price >= 0),
    CONSTRAINT chk_courses_level CHECK (level IN ('LOW', 'MIDDLE', 'HIGH')),
    CONSTRAINT chk_courses_status CHECK (status IN ('ACTIVE', 'DELETED'))
);

CREATE TABLE contents
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    course_id    BIGINT       NOT NULL,
    title        VARCHAR(50)  NOT NULL,
    body         VARCHAR(200) NOT NULL,
    content_type VARCHAR(20)  NOT NULL,
    seq          INT          NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   DATETIME(6)  NOT NULL,
    modified_at  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_contents_course FOREIGN KEY (course_id) REFERENCES courses (id),
    CONSTRAINT chk_contents_content_type CHECK (content_type IN ('VIDEO', 'TEXT', 'FILE')),
    CONSTRAINT chk_contents_seq CHECK (seq > 0),
    CONSTRAINT chk_contents_status CHECK (status IN ('ACTIVE', 'DELETED'))
);

CREATE INDEX idx_courses_status ON courses (status);
CREATE INDEX idx_contents_course_id_status_seq ON contents (course_id, status, seq);
CREATE INDEX idx_instructors_status ON instructors (status);
