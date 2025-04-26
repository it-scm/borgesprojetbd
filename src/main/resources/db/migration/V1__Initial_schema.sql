-- Create table utilisateur (base class for Etudiant and Professeur)
CREATE TABLE utilisateur (
                             id BIGSERIAL PRIMARY KEY,
                             email VARCHAR(255) NOT NULL UNIQUE,
                             nom VARCHAR(255) NOT NULL,
                             prenom VARCHAR(255) NOT NULL,
                             password VARCHAR(255) NOT NULL,
                             role VARCHAR(50) NOT NULL
);

-- Create table section
CREATE TABLE section (
                         id BIGSERIAL PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         nb_places INT NOT NULL
);

-- Create table annee_section
CREATE TABLE annee_section (
                               id BIGSERIAL PRIMARY KEY,
                               annee_academique VARCHAR(9) NOT NULL,
                               section_id BIGINT NOT NULL,
                               CONSTRAINT fk_annee_section_section FOREIGN KEY (section_id) REFERENCES section(id)
);

-- Create table etudiant (inherits from utilisateur)
CREATE TABLE etudiant (
                          id BIGINT PRIMARY KEY, -- Same id as utilisateur
                          section_id BIGINT,
                          annee_section_id BIGINT,
                          info TEXT,
                          photo TEXT,
                          CONSTRAINT fk_etudiant_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id),
                          CONSTRAINT fk_etudiant_section FOREIGN KEY (section_id) REFERENCES section(id),
                          CONSTRAINT fk_etudiant_annee_section FOREIGN KEY (annee_section_id) REFERENCES annee_section(id)
);

-- Create table professeur (inherits from utilisateur)
CREATE TABLE professeur (
                            id BIGINT PRIMARY KEY, -- Same id as utilisateur
                            matricule VARCHAR(255) NOT NULL UNIQUE,
                            CONSTRAINT fk_professeur_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id)
);

-- Create table cours
CREATE TABLE cours (
                       id BIGSERIAL PRIMARY KEY,
                       code VARCHAR(50) NOT NULL,
                       intitule VARCHAR(255) NOT NULL,
                       credits INT NOT NULL,
                       professeur_id BIGINT NOT NULL,
                       CONSTRAINT fk_cours_professeur FOREIGN KEY (professeur_id) REFERENCES professeur(id)
);

-- Create table horaire
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

-- Create table inscription (only after etudiant and cours are created)
CREATE TABLE inscription (
                             id BIGSERIAL PRIMARY KEY,
                             date_inscription DATE NOT NULL,
                             etudiant_id BIGINT NOT NULL,
                             cours_id BIGINT NOT NULL,
                             CONSTRAINT fk_inscription_etudiant FOREIGN KEY (etudiant_id) REFERENCES etudiant(id),
                             CONSTRAINT fk_inscription_cours FOREIGN KEY (cours_id) REFERENCES cours(id)
);
