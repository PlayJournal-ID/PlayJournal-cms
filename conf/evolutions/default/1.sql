# users schema
 
# --- !Ups

CREATE TABLE users (
    id         bigint		NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email      varchar(255)	NOT NULL UNIQUE,
    password   text			NOT NULL,
    name       text			NOT NULL
);

# --- !Downs

DROP TABLE users;