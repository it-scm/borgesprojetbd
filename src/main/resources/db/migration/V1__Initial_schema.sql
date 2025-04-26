-- Schoolman Schema - V1 (PostgreSQL version)

CREATE TABLE utilisateur (
                             id BIGSERIAL PRIMARY KEY,
                             email VARCHAR(255) NOT NULL UNIQUE,
                             nom VARCHAR(255) NOT NULL,
                             password VARCHAR(255) NOT NULL,
                             prenom VARCHAR(255) NOT NULL,
                             role VARCHAR(50) NOT NULL
);

CREATE TABLE section (
                         id BIGSERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         nb_places INT NOT NULL
);

CREATE TABLE annee_section (
                               id BIGSERIAL PRIMARY KEY,
                               annee_academique VARCHAR(9) NOT NULL,
                               section_id BIGINT NOT NULL,
                               CONSTRAINT fk_annee_section_section FOREIGN KEY (section_id) REFERENCES section(id)
);

CREATE TABLE professeur (
                            id BIGSERIAL PRIMARY KEY,
                            nom VARCHAR(255) NOT NULL,
                            prenom VARCHAR(255) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE,
                            password VARCHAR(255) NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            matricule VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE cours (
                       id BIGSERIAL PRIMARY KEY,
                       code VARCHAR(50) NOT NULL,
                       intitule VARCHAR(255) NOT NULL,
                       credits INT NOT NULL,
                       professeur_id BIGINT NOT NULL,
                       CONSTRAINT fk_cours_professeur FOREIGN KEY (professeur_id) REFERENCES professeur(id)
);

CREATE TABLE etudiant (
                          id BIGSERIAL PRIMARY KEY,
                          nom VARCHAR(255) NOT NULL,
                          prenom VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(50) NOT NULL,
                          section_id BIGINT,
                          CONSTRAINT fk_etudiant_section FOREIGN KEY (section_id) REFERENCES section(id)
);

CREATE TABLE horaire (
                         id BIGSERIAL PRIMARY KEY,
                         jour VARCHAR(50) NOT NULL,
                         heure_debut VARCHAR(10) NOT NULL,
                         heure_fin VARCHAR(10) NOT NULL,
                         cours_id BIGINT NOT NULL,
                         annee_section_id BIGINT NOT NULL,
                         CONSTRAINT fk_horaire_cours FOREIGN KEY (cours_id) REFERENCES cours(id),
                         CONSTRAINT fk_horaire_annee_section FOREIGN KEY (annee_section_id) REFERENCES annee_section(id)
);
