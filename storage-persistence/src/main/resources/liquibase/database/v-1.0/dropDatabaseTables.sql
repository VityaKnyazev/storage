ALTER table IF EXISTS storehouse DROP CONSTRAINT fk_good;
DROP table IF EXISTS  storehouse;
DROP TYPE IF EXISTS unit_enum;

ALTER table IF EXISTS goods 
DROP CONSTRAINT fk_category, 
DROP CONSTRAINT  fk_producer;
DROP table IF EXISTS goods;

DROP table IF EXISTS producers;
DROP table IF EXISTS categories;