create extension if not exists hstore;
create table phrases (id serial, key varchar(64) unique, translations hstore);
