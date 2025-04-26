-- V2__Insert_initial_data.sql

-- Insert Professeurs
INSERT INTO professeur (nom, prenom, email, password, role, matricule, created_at, updated_at)
VALUES
    ('Bernair', 'Michel', 'michel.bernair@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-1000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Hecquet', 'Jean-Paul', 'jean-paul.hecquet@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-1000000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Jaghou', 'Ali', 'ali.jaghou@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-1000000003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Lemaire', 'David', 'david.lemaire@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-1000000004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Sections
INSERT INTO section (nom, nb_places, created_at, updated_at)
VALUES
    ('Télécom', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Cyber', 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Électronique', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert AnneeSection
INSERT INTO annee_section (annee_academique, section_id, created_at, updated_at)
VALUES
    ('2024-2025', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('2024-2025', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('2024-2025', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Cours (assigning to the first Professeur created, ID = 1)
INSERT INTO cours (code, intitule, credits, professeur_id, created_at, updated_at)
VALUES
    ('UE299', 'Bases des réseaux', 5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('UE301', 'Bases de données', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('UE302', 'Analyse informatique', 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Horaire (using first Cours ID 1 and AnneeSection ID 1)
INSERT INTO horaire (jour, heure_debut, heure_fin, cours_id, annee_section_id, created_at, updated_at)
VALUES
    ('Lundi', '08h00', '10h00', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Etudiants
INSERT INTO etudiant (nom, prenom, email, password, role, section_id, created_at, updated_at)
VALUES
    ('Dupont', 'Alice', 'alice.dupont@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'ROLE_ETUDIANT', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Martin', 'Bob', 'bob.martin@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'ROLE_ETUDIANT', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Petit', 'Charlie', 'charlie.petit@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'ROLE_ETUDIANT', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
