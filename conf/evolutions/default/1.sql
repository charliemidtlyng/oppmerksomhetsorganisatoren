# Tasks schema

# --- !Ups
CREATE SEQUENCE adresse_id_seq;
CREATE TABLE adresse (
    id bigint NOT NULL DEFAULT nextval('adresse_id_seq') PRIMARY KEY,
    adressenavn varchar(255),
    postnummer varchar(4),
    poststed varchar(255)
);

CREATE SEQUENCE person_id_seq;
CREATE TABLE person (
    id bigint NOT NULL DEFAULT nextval('person_id_seq') PRIMARY KEY,
    navn varchar(64),
    fodselsdato timestamp,
    adresseId bigint REFERENCES adresse(id),
    info varchar(255)
);

# --- !Downs
DROP TABLE adresse;
DROP SEQUENCE adresse_id_seq;

DROP TABLE person;
DROP SEQUENCE person_id_seq;

