# Tasks schema

# --- !Ups
CREATE SEQUENCE oppmerksomhet_id_seq;
CREATE TABLE oppmerksomhet (
    id bigint NOT NULL DEFAULT nextval('oppmerksomhet_id_seq') PRIMARY KEY,
    til_id bigint,
    tilType varchar(32),
    fra_id bigint,
    fraType varchar(32),
    url varchar(255),
    info varchar(255),
    verdi double,
    tid timestamp,
    hendelsestype varchar(32),
    levert boolean,
    rolle varchar(32)
);

# --- !Downs
DROP TABLE oppmerksomhet;
DROP SEQUENCE oppmerksomhet_id_seq;
