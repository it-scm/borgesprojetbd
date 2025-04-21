-- =========================
-- TABLES
-- =========================

CREATE TABLE utilisateur (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nom VARCHAR(255),
                             prenom VARCHAR(255),
                             email VARCHAR(255),
                             password VARCHAR(255),
                             role VARCHAR(50)
);

CREATE TABLE etudiant (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          section_id BIGINT,
                          annee_section_id BIGINT,
                          info VARCHAR(255),
                          photo VARCHAR(255)
);

CREATE TABLE professeur (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            matricule VARCHAR(255)
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
-- UTILISATEURS
-- =========================

-- Étudiant (id = 1)
INSERT INTO utilisateur (nom, prenom, email, password, role)
VALUES (

           'Etudiant',
           'Inscrit',
           'etudiant.inscrit@ecole.be',
           '$2a$10$LwISU.4eHQYCC1LO0UnQqOJhtp1G09BgZrmC3Zr1GKHcMuRxZ.1v2',
           'ROLE_ETUDIANT'
       );

-- Professeur (id = 2)
INSERT INTO utilisateur (nom, prenom, email, password, role)
VALUES (

           'Jean',
           'Professeur',
           'professeur@ecole.be',
           '$2b$12$9t6m7zE8NT5RaLtBfArr1u0aFCx7WbimogMzCa90yJtfuMz4jpXve', -- professeur123
           'ROLE_PROFESSEUR'
       );

INSERT INTO professeur (id, matricule)
VALUES (2, 'ABC123');

-- =========================
-- COURS
-- =========================

INSERT INTO cours (code, intitule, credits, description, professeur_id)
VALUES
    ('CYB101', 'Introduction à la cybersécurité', 5, 'Cours de base sur les principes de cybersécurité.', 2),
    ('TEL202', 'Réseaux télécoms avancés', 6, 'Analyse et configuration des réseaux télécoms.', 2);
