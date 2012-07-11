# Add post table

# --- !Ups

CREATE TABLE post(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    writer BIGINT,
    INDEX post_title (title),
    INDEX post_writer (writer),
    FOREIGN KEY (writer) REFERENCES users(id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

# --- !Downs

DROP TABLE post;