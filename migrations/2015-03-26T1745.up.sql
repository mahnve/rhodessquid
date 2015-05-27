CREATE TABLE lang_codes (
        id serial PRIMARY KEY,
        lang_code varchar(10) UNIQUE NOT NULL
        );

CREATE TABLE keys (
        id serial PRIMARY KEY,
        key_name varchar(64) NOT NULL
        );

CREATE TABLE phrases (
        phrase varchar(512) NOT NULL,
        lang_code_id integer NOT NULL REFERENCES lang_codes(id),
        key_id integer NOT NULL REFERENCES keys(id),
        PRIMARY KEY(lang_code_id, key_id)
        );
