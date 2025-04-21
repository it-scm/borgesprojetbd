
-- =========================
-- DROP TABLES (Order matters for FK constraints)
-- =========================
DROP TABLE IF EXISTS cours;
DROP TABLE IF EXISTS professeur;
DROP TABLE IF EXISTS etudiant;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS section;

-- =========================
-- CREATE TABLES
-- =========================

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

CREATE TABLE etudiant (
                          id BIGINT PRIMARY KEY,
                          section_id BIGINT,
                          annee_section_id BIGINT,
                          info VARCHAR(255),
                          photo VARCHAR(255),
                          CONSTRAINT fk_etudiant_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id),
                          CONSTRAINT fk_etudiant_section FOREIGN KEY (section_id) REFERENCES section(id)
);

CREATE TABLE professeur (
                            id BIGINT PRIMARY KEY,
                            matricule VARCHAR(255),
                            CONSTRAINT fk_professeur_utilisateur FOREIGN KEY (id) REFERENCES utilisateur(id)
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

-- =========================
-- INSERT SECTIONS
-- =========================

INSERT INTO section (nom, nb_places) VALUES
                                         ('Cyber', 30),
                                         ('Telecom', 25),
                                         ('Electronique', 20);

-- =========================
-- INSERT UTILISATEURS
-- =========================

-- Étudiant inscrit (id = 1)
INSERT INTO utilisateur (id, nom, prenom, email, password, role) VALUES
    (1, 'Etudiant', 'Inscrit', 'etudiant.inscrit@ecole.be', '$2a$10$LwISU.4eHQYCC1LO0UnQqOJhtp1G09BgZrmC3Zr1GKHcMuRxZ.1v2', 'ROLE_ETUDIANT');

-- Étudiant non inscrit (id = 3)
INSERT INTO utilisateur (id, nom, prenom, email, password, role) VALUES
    (3, 'Etudiant', 'NonInscrit', 'etudiant.noninscrit@ecole.be', '$2a$10$LwISU.4eHQYCC1LO0UnQqOJhtp1G09BgZrmC3Zr1GKHcMuRxZ.1v2', 'ROLE_ETUDIANT');

-- Professeur (id = 2)
INSERT INTO utilisateur (id, nom, prenom, email, password, role) VALUES
    (2, 'Jean', 'Professeur', 'professeur@ecole.be', '$2b$12$9t6m7zE8NT5RaLtBfArr1u0aFCx7WbimogMzCa90yJtfuMz4jpXve', 'ROLE_PROFESSEUR');

-- =========================
-- INSERT ÉTUDIANTS / PROFESSEURS
-- =========================

-- Etudiant inscrit dans section Cyber (id section = 1)
INSERT INTO etudiant (id, section_id, annee_section_id, info, photo) VALUES
    (1, 1, NULL, NULL, NULL);

-- Etudiant non inscrit (pas de section)
INSERT INTO etudiant (id, section_id, annee_section_id, info, photo) VALUES
    (3, NULL, NULL, NULL, NULL);

INSERT INTO professeur (id, matricule) VALUES
    (2, 'ABC123');

-- =========================
-- INSERT COURS
-- =========================

INSERT INTO cours (code, intitule, credits, description, professeur_id) VALUES
    ('CYB101', 'Introduction à la cybersécurité', 5, 'Cours de base sur les principes de cybersécurité.', 2),
    ('TEL202', 'Réseaux télécoms avancés', 6, 'Analyse et configuration des réseaux télécoms.', 2);