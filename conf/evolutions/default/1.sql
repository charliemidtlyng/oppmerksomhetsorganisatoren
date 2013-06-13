# Tasks schema

# --- !Ups
CREATE SEQUENCE adresse_person_id_seq;
CREATE TABLE adresse (
    id bigint NOT NULL DEFAULT nextval('adresse_person_id_seq') PRIMARY KEY,
    familienavn varchar(255),
    adressenavn varchar(255),
    postnummer varchar(4),
    poststed varchar(255)
);

CREATE TABLE person (
    id bigint NOT NULL DEFAULT nextval('adresse_person_id_seq') PRIMARY KEY,
    navn varchar(64),
    fodselsdato timestamp,
    adresseId bigint REFERENCES adresse(id),
    info varchar(255)
);

# --- !Downs
DROP TABLE adresse;

DROP TABLE person;
DROP SEQUENCE adresse_person_id_seq;

