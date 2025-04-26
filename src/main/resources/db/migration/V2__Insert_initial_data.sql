-- Insert sections
INSERT INTO section (nom, nb_places) VALUES
                                         ('Télécom', 30),
                                         ('Cyber', 25),
                                         ('Électronique', 20);

-- Insert professeurs
INSERT INTO professeur (nom, prenom, email, password, role, matricule) VALUES
                                                                           ('Bernair', 'Michel', 'michel.bernair@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-10001'),
                                                                           ('Hecquet', 'Jean-Paul', 'jean-paul.hecquet@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-10002'),
                                                                           ('Jaghou', 'Ali', 'ali.jaghou@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-10003'),
                                                                           ('Lemaire', 'David', 'david.lemaire@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'PROFESSEUR', 'R-10004');

-- Insert cours
INSERT INTO cours (code, intitule, credits, professeur_id) VALUES
                                                               ('UE299', 'Bases des réseaux', 5, 1),
                                                               ('UE301', 'Bases de données', 4, 1),
                                                               ('UE302', 'Analyse informatique', 4, 1);

-- Insert annee_section
INSERT INTO annee_section (annee_academique, section_id) VALUES
                                                             ('2024-2025', 1),
                                                             ('2024-2025', 2),
                                                             ('2024-2025', 3);

-- Insert étudiants
INSERT INTO etudiant (nom, prenom, email, password, role, section_id) VALUES
                                                                          ('Dupont', 'Alice', 'alice.dupont@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'ROLE_ETUDIANT', 1),
                                                                          ('Martin', 'Bob', 'bob.martin@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'ROLE_ETUDIANT', NULL),
                                                                          ('Petit', 'Charlie', 'charlie.petit@ecole.be', '$2a$10$Dow1CQYZdJZxJ9b45FDEle6HiZK2MGrHE2f3BvF4lWwMcwG3DZBBa', 'ROLE_ETUDIANT', NULL);

-- Insert horaire
INSERT INTO horaire (jour, heure_debut, heure_fin, cours_id, annee_section_id) VALUES
    ('Lundi', '08h00', '10h00', 1, 1);
