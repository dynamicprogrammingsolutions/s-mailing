-- auto-generated definition
CREATE TABLE ApplicationUser
(
  id           BIGINT AUTO_INCREMENT
    PRIMARY KEY,
  username     VARCHAR(256) NOT NULL,
  passwordHash VARCHAR(256) NULL,
  CONSTRAINT application_users_id_uindex
  UNIQUE (id),
  CONSTRAINT application_users_username_uindex
  UNIQUE (username)
);
