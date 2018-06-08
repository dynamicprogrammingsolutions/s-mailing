create table ApplicationUser (id  bigserial not null, accessLevel int4, passwordHash varchar(255), username varchar(255) not null, primary key (id))
