# Add siteinfo for site name and about

# --- !Ups

CREATE TABLE siteinfo(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    about TEXT NOT NULL
);

# --- !Downs

DROP TABLE siteinfo;