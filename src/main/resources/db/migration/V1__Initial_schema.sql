-- ================================
-- V1__Initial_schema.sql
-- PostgreSQL schema for Gestion Ecole
-- ================================

CREATE TABLE utilisateur (
                             id BIGSERIAL PRIMARY KEY,
                             nom VARCHAR(255) NOT NULL,
                             prenom VARCHAR(255) NOT NULL,
                             email VARCHAR(255) UNIQUE NOT NULL,
                             password VARCHAR(255) NOT NULL,
                             role VARCHAR(255) NOT NULL,
                             matricule VARCHAR(255) -- 🔥 add this field
);


CREATE TABLE section (
                         id BIGSERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         nb_places INT NOT NULL
);

CREATE TABLE annee_section (
                               id BIGSERIAL PRIMARY KEY,
                               annee_academique VARCHAR(255) NOT NULL,
                               section_id BIGINT,
                               CONSTRAINT fk_annee_section_section FOREIGN KEY (section_id) REFERENCES section(id)
);


CREATE TABLE etudiant (
                          id BIGINT PRIMARY KEY,
                          section_id BIGINT,
                          annee_section_id BIGINT,
                          info TEXT,
                          photo TEXT,
                          CONSTRAINT fk_etudiant_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id),
                          CONSTRAINT fk_etudiant_section FOREIGN KEY (section_id) REFERENCES section(id),
                          CONSTRAINT fk_etudiant_annee_section FOREIGN KEY (annee_section_id) REFERENCES annee_section(id)
);

CREATE TABLE professeur (
                            id BIGINT PRIMARY KEY,
                            matricule VARCHAR(100) NOT NULL,
                            CONSTRAINT fk_professeur_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id)
);

CREATE TABLE cours (
                       id BIGSERIAL PRIMARY KEY,
                       code VARCHAR(20) NOT NULL,
                       intitule VARCHAR(255) NOT NULL,
                       description TEXT,
                       credits INT NOT NULL,
                       professeur_id BIGINT,
                       CONSTRAINT fk_cours_professeur FOREIGN KEY (professeur_id) REFERENCES professeur(id)
);


CREATE TABLE horaire (
                         id BIGSERIAL PRIMARY KEY,
                         jour_semaine VARCHAR(20) NOT NULL,
                         heure_debut TIME NOT NULL,
                         heure_fin TIME NOT NULL,
                         cours_id BIGINT,
                         annee_section_id BIGINT,
                         CONSTRAINT fk_horaire_cours FOREIGN KEY (cours_id) REFERENCES cours(id),
                         CONSTRAINT fk_horaire_annee_section FOREIGN KEY (annee_section_id) REFERENCES annee_section(id)
);

CREATE TABLE inscription (
                             id BIGSERIAL PRIMARY KEY,
                             etudiant_id BIGINT,
                             cours_id BIGINT,
                             date_inscription TIMESTAMP,
                             CONSTRAINT fk_inscription_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant(id),
                             CONSTRAINT fk_inscription_cours FOREIGN KEY (cours_id) REFERENCES cours(id)
);


CREATE TABLE note (
                      id BIGSERIAL PRIMARY KEY,
                      etudiant_id bigint NOT NULL,
                      cours_id bigint NOT NULL,
                      premiere_session DOUBLE PRECISION,
                      deuxieme_session DOUBLE PRECISION,
                      CONSTRAINT uk_note_etudiant_cours UNIQUE (etudiant_id, cours_id),
                      CONSTRAINT fk_note_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant(id),
                      CONSTRAINT fk_note_cours FOREIGN KEY (cours_id) REFERENCES cours(id)
);

