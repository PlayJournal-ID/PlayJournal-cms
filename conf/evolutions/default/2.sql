# add privilege

# --- !Ups

CREATE TABLE privilege(
    id bigint primary key,
    description text not null
);

ALTER TABLE users ADD privilege bigint;
ALTER TABLE users 
	ADD CONSTRAINT user_privilege 
	FOREIGN KEY (privilege) REFERENCES privilege(id) 
	ON UPDATE CASCADE ON DELETE SET NULL;
	
# --- !Downs

ALTER TABLE users DROP FOREIGN KEY user_privilege;
ALTER TABLE users DROP COLUMN privilege;
DROP TABLE privilege;
