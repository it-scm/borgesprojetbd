-- ================================
-- V2__Insert_initial_data.sql
-- Initial sample data for Gestion Ecole
-- ================================

-- Insert sections
INSERT INTO section (nom, nb_places) VALUES
                                         ('Télécom', 30),
                                         ('Cyber', 25),
                                         ('Électronique', 20);

-- Insert academic years
INSERT INTO annee_section (annee, section_id) VALUES
                                                  (2024, 1),
                                                  (2024, 2),
                                                  (2024, 3);

-- Insert students (utilisateur first)
INSERT INTO utilisateur (nom, prenom, email, password, role) VALUES
                                                                 ('Dupont', 'Alice', 'alice.dupont@ecole.be', '$2a$12$DummyHash1', 'ROLE_ETUDIANT'),
                                                                 ('Martin', 'Bob', 'bob.martin@ecole.be', '$2a$12$DummyHash2', 'ROLE_ETUDIANT'),
                                                                 ('Petit', 'Charlie', 'charlie.petit@ecole.be', '$2a$12$DummyHash3', 'ROLE_ETUDIANT');

-- Insert professors (utilisateur first)
INSERT INTO utilisateur (nom, prenom, email, password, role) VALUES
                                                                 ('Bernair', 'Michel', 'michel.bernair@ecole.be', '$2a$12$DummyHash4', 'ROLE_PROFESSEUR'),
                                                                 ('Hecquet', 'Jean-Paul', 'jean-paul.hecquet@ecole.be', '$2a$12$DummyHash5', 'ROLE_PROFESSEUR'),
                                                                 ('Jaghou', 'Ali', 'ali.jaghou@ecole.be', '$2a$12$DummyHash6', 'ROLE_PROFESSEUR'),
                                                                 ('Lemaire', 'David', 'david.lemaire@ecole.be', '$2a$12$DummyHash7', 'ROLE_PROFESSEUR');

-- Insert etudiants (after utilisateur insertions)
INSERT INTO etudiant (id, section_id, annee_section_id, info, photo) VALUES
                                                                         (1, 1, 1, NULL, NULL),
                                                                         (2, NULL, NULL, NULL, NULL),
                                                                         (3, NULL, NULL, NULL, NULL);

-- Insert professeurs (after utilisateur insertions)
INSERT INTO professeur (id, matricule) VALUES
                                           (4, 'R-10001'),
                                           (5, 'R-10002'),
                                           (6, 'R-10003'),
                                           (7, 'R-10004');

-- Insert courses
INSERT INTO cours (code, intitule, credits, professeur_id) VALUES
                                                               ('UE299', 'Bases des réseaux', 5, 4),
                                                               ('UE301', 'Bases de données', 4, 4),
                                                               ('UE302', 'Analyse informatique', 4, 4);

-- Insert schedules (horaires)
INSERT INTO horaire (jour_semaine, heure_debut, heure_fin, cours_id, annee_section_id) VALUES
    ('Lundi', '08:00:00', '10:00:00', 1, 1);
