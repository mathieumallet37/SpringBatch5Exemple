DROP TABLE IF EXISTS personne;

CREATE TABLE personne  (
                         id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                         prenom VARCHAR(50),
                         nom VARCHAR(50),
                         age INTEGER,
                         is_active BOOLEAN,
                         date_creation DATE
);