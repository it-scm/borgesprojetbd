DROP TABLE IF EXISTS horaire;
DROP TABLE IF EXISTS cours;
DROP TABLE IF EXISTS professeur;
DROP TABLE IF EXISTS etudiant;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS annee_section;
DROP TABLE IF EXISTS section;

CREATE TABLE section (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         nb_places INT NOT NULL
);

CREATE TABLE utilisateur (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nom VARCHAR(255),
                             prenom VARCHAR(255),
                             email VARCHAR(255) UNIQUE,
                             password VARCHAR(255),
                             role VARCHAR(50)
);

CREATE TABLE professeur (
                            id BIGINT PRIMARY KEY,
                            matricule VARCHAR(255),
                            CONSTRAINT fk_professeur_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id)
);

CREATE TABLE annee_section (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               annee_academique VARCHAR(255),
                               section_id BIGINT,
                               CONSTRAINT fk_annee_section_section FOREIGN KEY (section_id) REFERENCES section(id)
);

CREATE TABLE cours (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       code VARCHAR(50),
                       intitule VARCHAR(255),
                       credits INT,
                       description VARCHAR(500),
                       professeur_id BIGINT,
                       CONSTRAINT fk_cours_prof FOREIGN KEY (professeur_id) REFERENCES professeur(id)
);

CREATE TABLE etudiant (
                          id BIGINT PRIMARY KEY,
                          section_id BIGINT,
                          annee_section_id BIGINT,
                          info VARCHAR(255),
                          photo VARCHAR(255),
                          CONSTRAINT fk_etudiant_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id),
                          CONSTRAINT fk_etudiant_section FOREIGN KEY (section_id) REFERENCES section(id),
                          CONSTRAINT fk_etudiant_annee_section FOREIGN KEY (annee_section_id) REFERENCES annee_section(id)
);

CREATE TABLE horaire (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         jour VARCHAR(50),
                         heure_debut VARCHAR(20),
                         heure_fin VARCHAR(20),
                         cours_id BIGINT,
                         annee_section_id BIGINT,
                         CONSTRAINT fk_horaire_cours FOREIGN KEY (cours_id) REFERENCES cours(id),
                         CONSTRAINT fk_horaire_annee_section FOREIGN KEY (annee_section_id) REFERENCES annee_section(id)
);
