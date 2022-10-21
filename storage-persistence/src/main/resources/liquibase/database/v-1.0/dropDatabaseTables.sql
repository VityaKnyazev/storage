ALTER table IF EXISTS purchases DROP CONSTRAINT fk_user;
ALTER table IF EXISTS purchases DROP CONSTRAINT fk_storehouse;
DROP table IF EXISTS  purchases;

ALTER table IF EXISTS users_roles DROP CONSTRAINT fk_user;
ALTER table IF EXISTS users_roles DROP CONSTRAINT fk_role;
DROP table IF EXISTS  users_roles;

DROP table IF EXISTS  users;
DROP table IF EXISTS  roles;

ALTER table IF EXISTS storehouse DROP CONSTRAINT fk_good;
DROP table IF EXISTS  storehouse;

ALTER table IF EXISTS goods 
DROP CONSTRAINT fk_category, 
DROP CONSTRAINT  fk_producer;
DROP table IF EXISTS goods;

DROP table IF EXISTS producers;
DROP table IF EXISTS categories;